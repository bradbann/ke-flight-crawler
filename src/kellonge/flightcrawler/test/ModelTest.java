package kellonge.flightcrawler.test;

import java.util.Date;

import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.model.manager.FlightScheduleManager;
import kellonge.flightcrawler.utils.DateTimeUtils;

public class ModelTest {
	public static void main(String[] args) {
		FlightSchedule flightSchedule = new FlightSchedule();
		flightSchedule.setAirLineName("1");
		flightSchedule.setDeptAirportName("1");
		flightSchedule.setDeptCityName("1");
		flightSchedule.setDeptTime(DateTimeUtils.parseTime("12:22:00"));
		flightSchedule.setArrAirportName("1");
		flightSchedule.setArrCityName("1");
		flightSchedule.setArrTime(DateTimeUtils.parseTime("12:22:00"));
		flightSchedule.setFlightNo("HU201");
		flightSchedule.setExpiredDate(new Date());
		flightSchedule.setCreateDate(new Date());
		flightSchedule.setFlag(-99);
		new FlightScheduleManager().saveFlightSchedule(flightSchedule);
	}
}