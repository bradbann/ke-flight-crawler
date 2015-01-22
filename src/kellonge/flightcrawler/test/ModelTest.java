package kellonge.flightcrawler.test;

import java.util.Date;

import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.model.manager.FlightScheduleManager;
import kellonge.flightcrawler.utils.DateTimeUtils;
import kellonge.flightcrawler.utils.Utility;

public class ModelTest {
	public static void main(String[] args) {
		FlightSchedule flightSchedule = new FlightScheduleManager()
				.getFlightScheduleByParam("CA4142 ", Utility.toSafeDateTime("2015-1-21"));
		System.out.println(flightSchedule);
	}
}