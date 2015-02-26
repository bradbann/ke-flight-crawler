package kellonge.flightcrawler.spider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import kellonge.flightcrawler.config.Configuration;
import kellonge.flightcrawler.model.City;
import kellonge.flightcrawler.model.manager.CityManager;
import kellonge.flightcrawler.model.manager.FlightScheduleManager;
import kellonge.flightcrawler.pipline.ScheduleCtripPipline;
import kellonge.flightcrawler.process.ScheduleCtripPageProcess;
import kellonge.flightcrawler.utils.ErrorUrlWriter;
import kellonge.flightcrawler.utils.SpiderStatusMonitor;

import org.apache.log4j.Logger;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;

public class ScheduleCtripSpider {

	private static Logger logger = Logger.getLogger(ScheduleCtripSpider.class);
	private static Spider flightCrawler = null;

	private static List<Request> getSpiderRequest() {
		List<City> citys = new CityManager().getCitys();
		List<Request> urls = new ArrayList<Request>();

		if (!Configuration.isUseCachedQueue()) {

			for (int i = 0; i < citys.size(); i++) {
				for (int j = 0; j < citys.size(); j++) {
					Request request = new Request(String.format(
							"http://flights.ctrip.com/schedule/%s.%s.html",
							citys.get(i).getCityCode1(), citys.get(j)
									.getCityCode1()));
					urls.add(request);
				}
			}

			// urls.add(new Request(
			// "http://flights.ctrip.com/schedule/kmg.bjs.html"));

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
		flightCrawler = Spider.create(new ScheduleCtripPageProcess());
		SpiderStatusMonitor spiderStatusMonitor = new SpiderStatusMonitor(
				flightCrawler);
		flightCrawler
				.setExitWhenComplete(true)
				.setUUID("flights.ctrip.com")
				.thread(Configuration.getThreadNum())
				.addRequest(urls.toArray(new Request[urls.size()]))
				.setScheduler(
						new FileCacheQueueScheduler(Configuration.getDataPath()))
				.addPipeline(new ScheduleCtripPipline())
				.addSpiderListener(listener);
		if (!Configuration.isUseCachedQueue()) {

			new FlightScheduleManager().updateFlightScheduleStatusBeforeFetch();
		}
		return flightCrawler;
	}

	private static SpiderListener listener = new SpiderListener() {

		@Override
		public void onSuccess(Request request) {
		}

		@Override
		public void onError(Request request) {
			ErrorUrlWriter.Print(request.getInitUrl());

		}

		@Override
		public void onFinish(Spider spider) {
			if (!Configuration.isUseCachedQueue()) {
				new FlightScheduleManager()
						.updateFlightScheduleStatusBeforeFetch();
			}
			if (Configuration.isAutoRestart()) {
				flightCrawler = null;
				flightCrawler = GetSpider();
				flightCrawler.start();
			}
		}
	};

}
