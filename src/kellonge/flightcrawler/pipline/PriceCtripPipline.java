package kellonge.flightcrawler.pipline;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import kellonge.flightcrawler.model.FlightInfo; 
import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.model.manager.AirportManager;
import kellonge.flightcrawler.model.manager.FlightInfoManager;
import kellonge.flightcrawler.model.manager.FlightScheduleManager;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class PriceCtripPipline implements Pipeline {

	@Override
	public void process(ResultItems resultItems, Task task) {

		try {
			List<FlightInfo> modelData = (List<FlightInfo>) resultItems
					.get("ModelData");
			Request request = (Request) resultItems.get("Request");
			if (modelData != null) {
				for (FlightInfo flightInfo : modelData) {
					FlightSchedule flightSchedule = new FlightScheduleManager()
							.getFlightScheduleByParam(flightInfo.getFlightNo(),
									flightInfo.getDeptCityCode(),
									flightInfo.getArrCityCode(),
									flightInfo.getFlightDate());
					if (flightSchedule != null) {
						flightInfo.setFlightScheduleID(flightSchedule.getID());
						new FlightInfoManager().saveFlightInfo(flightInfo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
