package kellonge.flightcrawler.test;

import java.util.Date;

import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.model.manager.FlightInfoManager;
import kellonge.flightcrawler.model.manager.FlightScheduleManager;
import kellonge.flightcrawler.utils.DateTimeUtils;
import kellonge.flightcrawler.utils.Utility;

public class ModelTest {
	public static void main(String[] args) {
		new FlightInfoManager().updateFlightInfoReferenceByParam("1",
				"1,1,1,1,1,1,1", "8a2b45844b0d2d8d014b0d3b6a870007");
	}
}