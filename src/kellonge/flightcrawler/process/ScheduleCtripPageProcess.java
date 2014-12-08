package kellonge.flightcrawler.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import kellonge.flightcrawler.config.Configuration;
import kellonge.flightcrawler.model.City;
import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.utils.DateTimeUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class ScheduleCtripPageProcess implements PageProcessor {
	private Random random = new Random();
	private String[] UserAgents;
	private Site site = Site.me()
			.setCycleRetryTimes(Configuration.getCycleRetryTimes())
			.setSleepTime(Configuration.getSleepTime())
			.setTimeOut(Configuration.getTimeOut());

	public ScheduleCtripPageProcess(List<String[]> httpProxyList,
			List<String> userAgents) {
		if (httpProxyList.size() > 0) {
			site.setHttpProxyPool(httpProxyList);
		}
		if (userAgents.size() > 0) {
			site.setUserAgent(userAgents.get(0));
			UserAgents = userAgents.toArray(new String[userAgents.size()]);
		}

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

}
