package kellonge.flightcrawler.spider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import kellonge.flightcrawler.config.Configuration;
import kellonge.flightcrawler.model.City;
import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.model.manager.CityManager;
import kellonge.flightcrawler.model.manager.FlightInfoManager;
import kellonge.flightcrawler.model.manager.FlightPriceManager;
import kellonge.flightcrawler.model.manager.FlightScheduleManager;
import kellonge.flightcrawler.pipline.PriceCtripPipline;
import kellonge.flightcrawler.process.PriceCtripProcess;
import kellonge.flightcrawler.utils.DateTimeUtils;
import kellonge.flightcrawler.utils.ErrorUrlWriter;

import org.apache.log4j.Logger;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;

public class PriceCtripSpider {

	private static Logger logger = Logger.getLogger(PriceCtripSpider.class);
	private static Spider flightCrawler = null;

	private static AtomicLong successCnt = new AtomicLong(0);
	private static AtomicLong errorCnt = new AtomicLong(0);

	private static List<Request> getSpiderRequest() {
		List<Request> urls = new ArrayList<Request>();
		int[] DayAheadIneterval = new int[] { 2, 4, 8, 16, 32 };
		if (!Configuration.isUseCachedQueue()) {
			List<FlightSchedule> flightSchedules = new FlightScheduleManager()
					.getFlightSchedules("", "", "", "");
			for (FlightSchedule flightSchedule : flightSchedules) {
				Date now = new Date();

				int DayAhead = 1;
				for (int i = 0; i <= DayAheadIneterval.length; i++) {
					if (i > 0) {
						DayAhead = DayAhead + DayAheadIneterval[i - 1];
					}
					while (!FlightScheduleManager.IsTodayFlight(flightSchedule,
							DateTimeUtils.addDay(now, DayAhead))) {
						DayAhead++;
					}
					String referer = String
							.format("http://flights.ctrip.com/booking/%s-%s-day-%s.html",
									new CityManager().getCityByName(
											flightSchedule.getDeptCityName())
											.getCityCode1(),
									new CityManager().getCityByName(
											flightSchedule.getArrCityName())
											.getCityCode1(), DayAhead);
					String url = String
							.format("http://flights.ctrip.com/domesticsearch/search/SearchFirstRouteFlights?DCity1=%s&ACity1=%s&SearchType=S&DDate1=%s",
									new CityManager().getCityByName(
											flightSchedule.getDeptCityName())
											.getCityCode1(),
									new CityManager().getCityByName(
											flightSchedule.getArrCityName())
											.getCityCode1(), DateTimeUtils
											.format(DateTimeUtils.addDay(now,
													DayAhead), "yyyy-MM-dd"));
					Request request = new Request(url);
					Map<String, String> heads = new HashMap<String, String>();
					heads.put("Referer", referer);
					request.setRequestHeads(heads);
					urls.add(request);
				}
			}

			try {
				Files.deleteIfExists(Paths.get(Configuration.getDataPath()
						+ "/flights.ctrip.com.urls.txt"));
				Files.deleteIfExists(Paths.get(Configuration.getDataPath()
						+ "/flights.ctrip.com.cursor.txt"));
			} catch (IOException e) {
				logger.warn("delete cached urls file error  " + e.getMessage());
			}
		}

		return urls;

	}

	public static Spider GetSpider() {
		List<Request> urls = getSpiderRequest();
		List<SpiderListener> listeners = new ArrayList<SpiderListener>();
		listeners.add(listener);
		flightCrawler = Spider.create(new PriceCtripProcess());
		flightCrawler
				.setExitWhenComplete(true)
				.setUUID("flights.ctrip.com")
				.thread(Configuration.getThreadNum())
				.addRequest(urls.toArray(new Request[urls.size()]))
				.setScheduler(
						new FileCacheQueueScheduler(Configuration.getDataPath()))
				.addPipeline(new PriceCtripPipline())
				.setSpiderListeners(listeners)
		// .addPipeline(new ConsolePipeline())
		;

		return flightCrawler;
	}

	private static SpiderListener listener = new SpiderListener() {

		@Override
		public void onSuccess(Request request) {
			logger.info("[success] url:" + request.getInitUrl());
			if (request.getExtra(Request.BIZSUCCESS) != null
					&& request.getExtra(Request.BIZSUCCESS).equals(1)) {
				successCnt.getAndIncrement();
			}
			System.out.println(getSipderStatus());
		}

		@Override
		public void onError(Request request) {
			errorCnt.getAndIncrement();
			ErrorUrlWriter.Print(request.getInitUrl());
			logger.info("[error] url:" + request.getInitUrl() + "\n");
			System.out.println(getSipderStatus());

		}

		@Override
		public void onFinish(Spider spider) {
			logger.info("all request has finish.");

		}
	};

	private static String getSipderStatus() {
		String str = "";
		MonitorableScheduler monitorableScheduler = (MonitorableScheduler) flightCrawler
				.getScheduler();
		str += "totalQueue:"
				+ monitorableScheduler.getTotalRequestsCount(flightCrawler);
		str += " leftQueue:"
				+ monitorableScheduler.getLeftRequestsCount(flightCrawler);
		str += " totalRequest:" + flightCrawler.getPageCount();
		str += " error:" + errorCnt.get();
		str += " success:" + successCnt.get();

		return str;
	}
}
