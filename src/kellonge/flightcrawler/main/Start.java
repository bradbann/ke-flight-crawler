package kellonge.flightcrawler.main;

import kellonge.flightcrawler.config.Configuration;
import kellonge.flightcrawler.process.ScheduleCtripPageProcess;
import kellonge.flightcrawler.spider.PriceCtripSpider;
import kellonge.flightcrawler.spider.ScheduleCtripSipder;

public class Start {
	public static void main(String[] args) {
		Configuration.init();
		ScheduleCtripSipder.GetSpider().start();
	}
}
