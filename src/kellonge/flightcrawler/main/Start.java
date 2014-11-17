package kellonge.flightcrawler.main;

import kellonge.flightcrawler.process.ScheduleCtripPageProcess;

public class Start {
	public static void main(String[] args) {
		ScheduleCtripPageProcess.GetSpider().start();
	}
}
