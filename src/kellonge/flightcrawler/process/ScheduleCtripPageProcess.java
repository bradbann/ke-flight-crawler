package kellonge.flightcrawler.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.JMException;

import org.hibernate.Session;

import kellonge.flightcrawler.model.City;
import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.pipline.SingleModelSavePipline;
import kellonge.flightcrawler.utils.DateTimeUtils;
import kellonge.flightcrawler.utils.HibernateUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class ScheduleCtripPageProcess implements PageProcessor {
	private Site site = Site
			.me()  
			.addHeader(
					"User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36")
			.setCycleRetryTimes(3).setRetryTimes(2).setSleepTime(6000)
			.setTimeOut(10000);

	public ScheduleCtripPageProcess() {
	 
	}

	@Override
	public void process(Page page) {
		City deptCity = (City) page.getRequest().getExtra("DeptCity");
		City arrCity = (City) page.getRequest().getExtra("ArrCity");
		List<Selectable> scheduleList = page.getHtml()
				.$(".result_m_content tr").nodes();
		List<FlightSchedule> flightList = new ArrayList<FlightSchedule>();
		for (Selectable schedule : scheduleList) {
			FlightSchedule flight = new FlightSchedule();
			flight.setFlag(1);
			flight.setDataSource("CTRIP");
			flight.setExpiredDate(DateTimeUtils.addDay(new Date(), 7));
			if (deptCity != null) {
				flight.setDeptCityID(deptCity.getID());
				flight.setDeptCityName(deptCity.getCityName());
			}
			if (arrCity != null) {
				flight.setArrCityID(arrCity.getID());
				flight.setArrCityName(arrCity.getCityName());
			}
			flight.setAirLineName(schedule.xpath(
					"//*[@class='flight_logo']/a/strong/@data-description")
					.get());
			flight.setFlightNo(schedule.xpath(
					"//*[@class='flight_logo']/a/strong/text()").get());
			flight.setPlanModel(schedule.xpath(
					"//*[@data-defer='fltTypeJmp']/@code").get());
			flight.setDeptAirportName(schedule
					.xpath("//*[@class='depart']/*[@class='airport']/@title")
					.regex("[\\u4e00-\\u9fa5]*").get());
			flight.setDeptTerminal(schedule
					.xpath("//*[@class='depart']/*[@class='airport']/@title")
					.regex("[a-zA-Z\\d]+").get());
			flight.setDeptTime(DateTimeUtils.parseTime(schedule.xpath(
					"//*[@class='depart']/*[@class='time']/text()").get()
					+ ":00"));
			flight.setArrAirportName(schedule
					.xpath("//*[@class='arrive']/*[@class='airport']/@title")
					.regex("[\\u4e00-\\u9fa5]*").get());
			flight.setArrTerminal(schedule
					.xpath("//*[@class='arrive']/*[@class='airport']/@title")
					.regex("[a-zA-Z\\d]+").get());
			flight.setArrTime(DateTimeUtils.parseTime(schedule.xpath(
					"//*[@class='arrive']/*[@class='time']/text()").get()
					+ ":00"));
			flight.setIsStopOver(schedule.xpath("//*[@class='stop']/div/span")
					.match() ? true : false);
			char[] weeks = new char[7];
			Arrays.fill(weeks, '0');
			List<Selectable> weekNodes = schedule.xpath(
					"//*[@class='weeks']/div/span").nodes();
			if (weekNodes.size() >= weeks.length) {
				for (int i = 0; i < weeks.length; i++) {
					weeks[i] = weekNodes.get(i).regex("class").match() ? '1'
							: '0';
				}
			}
			StringBuilder sb = new StringBuilder();
			for (char c : weeks) {
				sb.append("," + c);
			}
			flight.setWeekSchedule(sb.substring(1));
			flightList.add(flight);

		}
		if (flightList.size() == 0) {
			page.setSkip(true);
		}
		// page.putField("ModelData", flightList);

		// 分页
		Map<String, Object> extent = new HashMap<String, Object>();
		extent.put("DeptCity", deptCity);
		extent.put("ArrCity", arrCity);
		for (Selectable links : page.getHtml()
				.$(".schedule_page_list a:not(.current)").links().nodes()) {
			Request request = new Request(links.get());
			request.setExtras(extent);
			page.addTargetRequest(request);
		}
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static Spider GetSpider() {
		Session session = HibernateUtils.getSessionFactory()
				.getCurrentSession();
		session.beginTransaction();
		List<City> citys = (List<City>) session.createQuery(" from City")
				.list();
		session.getTransaction().commit();
		List<Request> urls = new ArrayList<Request>();

		for (int i = 0; i < citys.size(); i++) {
			for (int j = 0; j < citys.size(); j++) {
				if (i == j) {
					break;
				}
				Request request = new Request(String.format(
						"http://flights.ctrip.com/schedule/%s.%s.html", citys
								.get(i).getCityCode1(), citys.get(j)
								.getCityCode1()));
				Map<String, Object> extent = new HashMap<String, Object>();
				extent.put("DeptCity", citys.get(i));
				extent.put("ArrCity", citys.get(j));
				request.setExtras(extent);
				urls.add(request);
			}
		}

		Spider flightCrawler = Spider.create(new ScheduleCtripPageProcess())
				.thread(3).addRequest(urls.toArray(new Request[urls.size()]))
				.addPipeline(new SingleModelSavePipline<FlightSchedule>())
				.addPipeline(new ConsolePipeline());

		try {
			SpiderMonitor.instance().register(flightCrawler);
		} catch (JMException e) {
			e.printStackTrace();
		}
		return flightCrawler;
	}
}
