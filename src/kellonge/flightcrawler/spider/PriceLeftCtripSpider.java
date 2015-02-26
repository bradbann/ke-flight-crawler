package kellonge.flightcrawler.spider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

import kellonge.flightcrawler.config.Configuration;
import kellonge.flightcrawler.model.City;
import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.model.manager.CityManager;
import kellonge.flightcrawler.model.manager.FlightInfoManager; 
import kellonge.flightcrawler.model.manager.FlightScheduleManager;
import kellonge.flightcrawler.pipline.PriceCtripPipline;
import kellonge.flightcrawler.process.PriceCtripProcess;
import kellonge.flightcrawler.utils.DateTimeUtils;
import kellonge.flightcrawler.utils.ErrorUrlWriter;
import kellonge.flightcrawler.utils.SpiderStatusMonitor;

import org.apache.log4j.Logger;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;

public class PriceLeftCtripSpider {

	private static Logger logger = Logger.getLogger(PriceLeftCtripSpider.class);
	private static Spider flightCrawler = null;

	private static List<Request> getSpiderRequest() {
		List<Request> urls = new ArrayList<Request>();
		int[] DayAheadIneterval = new int[] { 2, 4, 8, 16, 32 };
		if (!Configuration.isUseCachedQueue()) {
			List<FlightSchedule> flightSchedules = new FlightScheduleManager()
				.getNoPriceFlightSchedules();
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
					if (DayAhead>60) {
						break;
					}
					String referer = "1";
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
		flightCrawler = Spider.create(new PriceCtripProcess());
		SpiderStatusMonitor spiderStatusMonitor = new SpiderStatusMonitor(
				flightCrawler);
		flightCrawler
				.setExitWhenComplete(true)
				.setUUID("flights.ctrip.com")
				.thread(Configuration.getThreadNum())
				.addRequest(urls.toArray(new Request[urls.size()]))
				.setScheduler(
						new FileCacheQueueScheduler(Configuration.getDataPath()))
				.addPipeline(new PriceCtripPipline())
				.addSpiderListener(listener);
		return flightCrawler;
	}

	private static SpiderListener listener = new SpiderListener() {

		@Override
		public void onSuccess(Request request) {
		}

		@Override
		public void onError(Request request) {
			ErrorUrlWriter.Print(request.toString());

		}

		@Override
		public void onFinish(Spider spider) {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					System.exit(0);
				}
			}, 1000*10);
		}
	};

}
