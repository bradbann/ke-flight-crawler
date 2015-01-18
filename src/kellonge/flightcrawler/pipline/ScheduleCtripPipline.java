package kellonge.flightcrawler.pipline;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import kellonge.flightcrawler.model.AirLine;
import kellonge.flightcrawler.model.Airport;
import kellonge.flightcrawler.model.City;
import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.model.manager.AirLineManager;
import kellonge.flightcrawler.model.manager.AirportManager;
import kellonge.flightcrawler.model.manager.CityManager;
import kellonge.flightcrawler.model.manager.FlightScheduleManager;
import kellonge.flightcrawler.utils.DateTimeUtils; 
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author kellonge 处理航班时刻保存
 * @param <T>
 *            实体类型
 */
public class ScheduleCtripPipline implements Pipeline {

	@Override
	public void process(ResultItems resultItems, Task task) {
		try {
			List<FlightSchedule> modelData = (List<FlightSchedule>) resultItems
					.get("ModelData");
			if (modelData != null) {
				for (FlightSchedule flightSchedule : modelData) {
					flightSchedule.setFlightInterval(DateTimeUtils
							.SubstractTime(flightSchedule.getDeptTime(),
									flightSchedule.getArrTime()));
					// city
					City deptCity = new CityManager()
							.getCityByName(flightSchedule.getDeptCityName());
					City arrCity = new CityManager()
							.getCityByName(flightSchedule.getArrCityName());
					if (deptCity != null) {
						flightSchedule.setDeptCityID(deptCity.getID());
					}
					if (arrCity != null) {
						flightSchedule.setArrCityID(arrCity.getID());
					}

					// save airline

					AirLine airLine = new AirLineManager()
							.getAirLineByName(flightSchedule.getAirLineName());
					if (airLine == null) {
						airLine = new AirLine();
						airLine.setName(flightSchedule.getAirLineName());
						if (flightSchedule.getFlightNo().length() >= 2) {
							airLine.setCode(flightSchedule.getFlightNo()
									.substring(0, 2));
						}
						new AirLineManager().saveAirLine(airLine);
					}
					flightSchedule.setAirLineID(airLine.getID());

					// save dept airport
					Airport deptAirport = new AirportManager()
							.getAirportByName(flightSchedule
									.getDeptAirportName());
					if (deptAirport == null) {
						deptAirport = new Airport();
						deptAirport
								.setName(flightSchedule.getDeptAirportName());
						new AirportManager().saveAirport(deptAirport);
					}
					flightSchedule.setDeptAirportID(deptAirport.getID());

					// save arr airport
					Airport arrAirport = new AirportManager()
							.getAirportByName(flightSchedule
									.getArrAirportName());
					if (arrAirport == null) {
						arrAirport = new Airport();
						arrAirport.setName(flightSchedule.getArrAirportName());
						new AirportManager().saveAirport(arrAirport);
					}
					flightSchedule.setArrAirportID(arrAirport.getID());

					new FlightScheduleManager()
							.saveFlightSchedule(flightSchedule);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
