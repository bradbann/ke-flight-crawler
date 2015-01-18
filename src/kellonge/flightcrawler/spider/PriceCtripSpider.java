package kellonge.flightcrawler.spider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.management.JMException;

import kellonge.flightcrawler.config.Configuration;
import kellonge.flightcrawler.extension.MultiRequestSpider;
import kellonge.flightcrawler.model.City;
import kellonge.flightcrawler.pipline.ScheduleCtripPipline; 
import kellonge.flightcrawler.process.PriceCtripProcess;
import kellonge.flightcrawler.process.ScheduleCtripPageProcess;
import kellonge.flightcrawler.utils.ErrorUrlWriter; 

import org.apache.log4j.Logger;
import org.hibernate.Session;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

public class PriceCtripSpider {

	private static Logger logger = Logger.getLogger(ScheduleCtripSipder.class);

	public static MultiRequestSpider GetSpider() {

		List<Request> urls = new ArrayList<Request>();

		Request request = new Request(
				String.format("http://flights.ctrip.com/booking/BJS-SHA-day-10.html"));
		urls.add(request);
		Spider flightCrawler = MultiRequestSpider.create(new PriceCtripProcess())
				.thread(Configuration.getThreadNum())
				.addRequest(urls.toArray(new Request[urls.size()]))
				.addPipeline(new ConsolePipeline());
		try {
			SpiderMonitor.instance().register(flightCrawler);
		} catch (JMException e) {
			e.printStackTrace();
		}
		return (MultiRequestSpider) flightCrawler;
	}

}