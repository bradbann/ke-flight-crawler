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

	private static AtomicLong successCnt = new AtomicLong(0);
	private static AtomicLong errorCnt = new AtomicLong(0);

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
		flightCrawler
				.setExitWhenComplete(true)
				.setUUID("flights.ctrip.com")
				.thread(Configuration.getThreadNum())
				.addRequest(urls.toArray(new Request[urls.size()]))
				.setScheduler(
						new FileCacheQueueScheduler(Configuration.getDataPath()))
				.addPipeline(new ScheduleCtripPipline())
				.setSpiderListeners(listeners)
		// .addPipeline(new ConsolePipeline())
		;
		new FlightScheduleManager().updateFlightScheduleStatusBeforeFetch();
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
			new FlightScheduleManager().updateFlightScheduleStatusAfterFetch();
			if (Configuration.isAutoRestart()) {
				errorCnt.set(0);
				successCnt.set(0);
				flightCrawler = null;
				flightCrawler = GetSpider();
				flightCrawler.start();
			}
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
