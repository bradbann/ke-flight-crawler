package kellonge.flightcrawler.spider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.management.JMException;

import kellonge.flightcrawler.config.Configuration;
import kellonge.flightcrawler.model.City;
import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.pipline.SingleModelSavePipline;
import kellonge.flightcrawler.process.ScheduleCtripPageProcess;
import kellonge.flightcrawler.utils.HibernateUtils;

import org.hibernate.Session;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

public class ScheduleCtripSipder {

	private static String filePath = Configuration.ROOT_PATH
			+ "/data/error.txt";
	private static PrintWriter fileUrlWriter;

	private static void initFlushThread() {
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				flush();
			}
		}, 10, 10, TimeUnit.SECONDS);
	}

	private static void flush() {
		fileUrlWriter.flush();
	}

	public static Spider GetSpider() {

		try {
			fileUrlWriter = new PrintWriter(new FileWriter(filePath, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
		initFlushThread();
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
				request.putExtra("DeptCity", citys.get(i));
				request.putExtra("ArrCity", citys.get(j));

				urls.add(request);
			}
		}
		PageProcessor processor = new ScheduleCtripPageProcess(
				Configuration.getProxys(), Configuration.getUserAgents());

		List<SpiderListener> listeners = new ArrayList<SpiderListener>();
		listeners.add(listener);
		Spider flightCrawler = Spider
				.create(processor)
				.thread(Configuration.getThreadNum())
				.addRequest(urls.toArray(new Request[urls.size()]))
				.setScheduler(
						new FileCacheQueueScheduler(Configuration.getDataPath()))
				.addPipeline(new SingleModelSavePipline<FlightSchedule>())
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
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(Request request) {
			fileUrlWriter.println(request.getUrl());

		}
	};
}
