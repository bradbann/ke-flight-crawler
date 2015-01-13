package kellonge.flightcrawler.spider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javassist.expr.NewArray;

import javax.management.JMException;

import kellonge.flightcrawler.config.Configuration;
import kellonge.flightcrawler.model.City;
import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.pipline.FlightSchedulePipline;
import kellonge.flightcrawler.process.ScheduleCtripPageProcess;
import kellonge.flightcrawler.utils.DateTimeUtils;
import kellonge.flightcrawler.utils.ErrorUrlWriter;
import kellonge.flightcrawler.utils.HibernateUtils;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

public class ScheduleCtripSipder {

	private static Logger logger = Logger.getLogger(ScheduleCtripSipder.class);

	public static Spider GetSpider() {
		Session session = HibernateUtils.getSessionFactory()
				.getCurrentSession();
		session.beginTransaction();
		List<City> citys = (List<City>) session.createQuery(" from City")
				.list();
		session.getTransaction().commit();
		List<Request> urls = new ArrayList<Request>();

		if (!Configuration.isUseCachedQueue()) {

			for (int i = 0; i < citys.size(); i++) {
				for (int j = 0; j < citys.size(); j++) {
					Request request = new Request(String.format(
							"http://flights.ctrip.com/schedule/%s.%s.html",
							citys.get(i).getCityCode1(), citys.get(j)
									.getCityCode1()));
					Map<String, Object> extent = new HashMap<String, Object>();
					extent.put("DeptCity", citys.get(i));
					extent.put("ArrCity", citys.get(j));
					request.setExtras(extent);
					request.putExtra("DeptCity", citys.get(i));
					request.putExtra("ArrCity", citys.get(j));

					urls.add(request);
				}
			}

			try {
				Files.deleteIfExists(Paths.get(Configuration.getDataPath()+"/flights.ctrip.com.urls.txt"));
				Files.deleteIfExists(Paths.get(Configuration.getDataPath()+"/flights.ctrip.com.cursor.txt"));
			} catch (IOException e) {
				logger.warn("delete cached urls file error  " + e.getMessage());
			}
		}

		List<SpiderListener> listeners = new ArrayList<SpiderListener>();
		listeners.add(listener);
		Spider flightCrawler = Spider
				.create(new ScheduleCtripPageProcess())
				.setUUID("flights.ctrip.com")
				.thread(Configuration.getThreadNum())
				.addRequest(urls.toArray(new Request[urls.size()]))
				.setScheduler(
						new FileCacheQueueScheduler(Configuration.getDataPath()))
				.addPipeline(new FlightSchedulePipline())
				.setSpiderListeners(listeners)
				.addPipeline(new ConsolePipeline());
		try {
			SpiderMonitor.instance().register(flightCrawler);
		} catch (JMException e) {
			e.printStackTrace();
		}
		return flightCrawler;
	}

	private static SpiderListener listener = new SpiderListener() {

		@Override
		public void onSuccess(Request request) {
			logger.info("[success] url:" + request.getUrl());

		}

		@Override
		public void onError(Request request) {
			ErrorUrlWriter.Print(request.getUrl());
			logger.info("[error] url:" + request.getUrl() + "\n");

		}
	};
}
