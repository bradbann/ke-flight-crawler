package kellonge.flightcrawler.spider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kellonge.flightcrawler.config.Configuration;
import kellonge.flightcrawler.extension.SpiderExtension;
import kellonge.flightcrawler.extension.SpiderListenerExtension;
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

public class PriceCtripSpider {

	private static Logger logger = Logger.getLogger(PriceCtripSpider.class);
	private static SpiderExtension flightCrawler = null;

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
						DayAhead = DayAhead + DayAheadIneterval[i-1];
					}
					while (!FlightScheduleManager.IsTodayFlight(flightSchedule,
							DateTimeUtils.addDay(now, DayAhead))) {
						DayAhead++;
					}

					Request request = new Request(
							String.format(
									"http://flights.ctrip.com/booking/%s-%s-day-%s.html",
									new CityManager().getCityByName(
											flightSchedule.getDeptCityName())
											.getCityCode1(),
									new CityManager().getCityByName(
											flightSchedule.getArrCityName())
											.getCityCode1(), DayAhead));
					urls.add(request);
				}
			}
			// urls.add(new Request(String
			// .format("http://flights.ctrip.com/booking/BJS-SHA-day-1.html")));
			//
			// urls.add(new Request(String
			// .format("http://flights.ctrip.com/booking/BJS-SHA-day-3.html")));
			//
			// urls.add(new Request(String
			// .format("http://flights.ctrip.com/booking/BJS-SHA-day-7.html")));
			// urls.add(new Request(String
			// .format("http://flights.ctrip.com/booking/kmg-SHA-day-10.html")));

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

	public static SpiderExtension GetSpider() {
		List<Request> urls = getSpiderRequest();
		List<SpiderListener> listeners = new ArrayList<SpiderListener>();
		listeners.add(listener);
		flightCrawler = SpiderExtension.create(new PriceCtripProcess());
		flightCrawler
				.setExitWhenComplete(true)
				.setUUID("flights.ctrip.com")
				.thread(Configuration.getThreadNum())
				.addRequest(urls.toArray(new Request[urls.size()]))
				.setScheduler(
						new FileCacheQueueScheduler(Configuration.getDataPath()))
				.addPipeline(new PriceCtripPipline())
				.setSpiderListeners(listeners)
				.addPipeline(new ConsolePipeline());
		return flightCrawler;
	}

	private static SpiderListenerExtension listener = new SpiderListenerExtension() {

		@Override
		public void onSuccess(Request request) {
			logger.info("[success] url:" + request.getUrl());
		}

		@Override
		public void onError(Request request) {
			ErrorUrlWriter.Print(request.getUrl());
			logger.info("[error] url:" + request.getUrl() + "\n");

		}

		@Override
		public void onFinish(Spider spider) {
			logger.info("all request has finish.");

		}
	};
}
