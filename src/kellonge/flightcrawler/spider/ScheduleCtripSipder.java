package kellonge.flightcrawler.spider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import kellonge.flightcrawler.config.Configuration;
import kellonge.flightcrawler.extension.SpiderExtension;
import kellonge.flightcrawler.extension.SpiderListenerExtension;
import kellonge.flightcrawler.model.City;
import kellonge.flightcrawler.model.manager.CityManager;
import kellonge.flightcrawler.model.manager.FlightInfoManager;
import kellonge.flightcrawler.model.manager.FlightScheduleManager;
import kellonge.flightcrawler.pipline.ScheduleCtripPipline;
import kellonge.flightcrawler.process.ScheduleCtripPageProcess;
import kellonge.flightcrawler.utils.ErrorUrlWriter;

import org.apache.log4j.Logger;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

public class ScheduleCtripSipder {

	private static Logger logger = Logger.getLogger(ScheduleCtripSipder.class);
	private static SpiderExtension flightCrawler = null;

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
		flightCrawler = SpiderExtension.create(new ScheduleCtripPageProcess());
		flightCrawler
				.setExitWhenComplete(true)
				.setUUID("flights.ctrip.com")
				.thread(Configuration.getThreadNum())
				.addRequest(urls.toArray(new Request[urls.size()]))
				.setScheduler(
						new FileCacheQueueScheduler(Configuration.getDataPath()))
				.addPipeline(new ScheduleCtripPipline())
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
			if (Configuration.isAutoRestart()) {
				flightCrawler = null;
				flightCrawler = GetSpider();
				flightCrawler.start();
			}
		}
	};
}
