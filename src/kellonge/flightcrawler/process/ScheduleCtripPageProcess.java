package kellonge.flightcrawler.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.selector.Selectable;

public class ScheduleCtripPageProcess implements PageProcessor {
	private Random random = new Random();
	private String[] UserAgents = new String[] {
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1",
			"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0) Gecko/20100101 Firefox/6.0",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
			"Opera/9.80 (Windows NT 6.1; U; zh-cn) Presto/2.9.168 Version/11.50",
			"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0; .NET CLR 2.0.50727; SLCC2; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C; Tablet PC 2.0; .NET4.0E)",
			"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; InfoPath.3)",
			"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; GTB7.0)",
			"Mozilla/5.0 (Windows; U; Windows NT 6.1; ) AppleWebKit/534.12 (KHTML, like Gecko) Maxthon/3.0 Safari/534.12",
			"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C; .NET4.0E)",
			"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.41 Safari/535.1 QQBrowser/6.9.11079.201",

	};
	private Site site = Site.me().setUserAgent(UserAgents[0])
			.setCycleRetryTimes(6).setSleepTime(2000)
			.setTimeOut(10000);

	public ScheduleCtripPageProcess() {
		List<String[]> httpProxyList = new ArrayList<String[]>();
		httpProxyList.add(new String[] { "122.72.99.3", "80" });
		httpProxyList.add(new String[] { "122.72.28.118", "80" });
		httpProxyList.add(new String[] { "122.72.28.119", "80" });
		httpProxyList.add(new String[] { "122.72.28.117", "80" });
		httpProxyList.add(new String[] { "183.207.229.11", "80" });
		httpProxyList.add(new String[] { "183.207.229.12", "80" });
		httpProxyList.add(new String[] { "183.207.229.13", "80" });
		httpProxyList.add(new String[] { "183.207.229.10", "80" });
		httpProxyList.add(new String[] { "183.207.228.8", "80" });
		httpProxyList.add(new String[] { "183.207.228.120", "80" });
		httpProxyList.add(new String[] { "183.207.229.137", "80" });
		httpProxyList.add(new String[] { "183.207.229.138", "80" });
		httpProxyList.add(new String[] { "183.207.224.51", "82" });
		httpProxyList.add(new String[] { "183.207.224.51", "83" });
		httpProxyList.add(new String[] { "183.207.224.51", "84" });
		httpProxyList.add(new String[] { "183.207.224.48", "80" });
		httpProxyList.add(new String[] { "122.72.124.42", "80" });
		httpProxyList.add(new String[] { "183.207.224.46", "80" });
		httpProxyList.add(new String[] { "113.214.13.1", "8000" });
		httpProxyList.add(new String[] { "183.207.224.51", "80" });
		httpProxyList.add(new String[] { "221.183.16.219", "80" });
		httpProxyList.add(new String[] { "183.207.224.50", "81" });
		httpProxyList.add(new String[] { "111.7.129.151", "80" });
		httpProxyList.add(new String[] { "111.7.129.140", "80" });
		httpProxyList.add(new String[] { "183.207.224.49", "80" });
		httpProxyList.add(new String[] { "183.207.224.52", "80" });
		httpProxyList.add(new String[] { "111.7.129.141", "80" });
		httpProxyList.add(new String[] { "58.68.129.68", "8888" });
		httpProxyList.add(new String[] { "120.198.243.115", "8888" });
		httpProxyList.add(new String[] { "183.207.229.13", "9000" });
		httpProxyList.add(new String[] { "183.207.229.138", "8090" });
		httpProxyList.add(new String[] { "183.207.229.203", "80" });
		httpProxyList.add(new String[] { "183.207.229.138", "8000" });
		httpProxyList.add(new String[] { "183.207.229.138", "8080" });
		httpProxyList.add(new String[] { "120.198.243.53", "80" });
		httpProxyList.add(new String[] { "117.146.116.69", "80" });
		httpProxyList.add(new String[] { "218.207.208.55", "8080" });
		httpProxyList.add(new String[] { "223.72.181.50", "3128" });
		httpProxyList.add(new String[] { "120.198.243.52", "80" });
		httpProxyList.add(new String[] { "183.207.229.194", "80" });
		httpProxyList.add(new String[] { "120.198.243.50", "80" });
		httpProxyList.add(new String[] { "112.17.0.202", "80" });
		httpProxyList.add(new String[] { "111.11.184.116", "80" });
		httpProxyList.add(new String[] { "111.11.184.119", "80" });
		httpProxyList.add(new String[] { "117.146.116.67", "80" });
		httpProxyList.add(new String[] { "117.146.116.68", "80" });
		httpProxyList.add(new String[] { "183.207.229.195", "80" });
		httpProxyList.add(new String[] { "112.17.0.204", "80" });
		httpProxyList.add(new String[] { "111.11.184.13", "80" });
		httpProxyList.add(new String[] { "117.169.64.4", "80" });
		site.setHttpProxyPool(httpProxyList);
	}

	@Override
	public void process(Page page) {
		site.setUserAgent(UserAgents[random.nextInt(UserAgents.length)]);
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
		} else {
			page.putField("ModelData", flightList);
		}
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

		System.out.println(citys.size());
		System.out.println(urls.size());
		Spider flightCrawler = Spider.create(new ScheduleCtripPageProcess())
				.thread(10).addRequest(urls.toArray(new Request[urls.size()]))
				.setScheduler(new FileCacheQueueScheduler("/home/kellonge/Test"))
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
