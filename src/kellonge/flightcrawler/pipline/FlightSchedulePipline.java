package kellonge.flightcrawler.pipline;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import kellonge.flightcrawler.model.AirLine;
import kellonge.flightcrawler.model.Airport;
import kellonge.flightcrawler.model.City;
import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.utils.DateTimeUtils;
import kellonge.flightcrawler.utils.HibernateUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author kellonge 处理航班时刻保存
 * @param <T>
 *            实体类型
 */
public class FlightSchedulePipline implements Pipeline {

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

					// save airline
					Session session = HibernateUtils.getSessionFactory()
							.getCurrentSession();
					session.beginTransaction();
					Query query = session
							.createQuery(" select a from AirLine  a where  a.Name = :Name");
					query.setString("Name", flightSchedule.getAirLineName());
					List<AirLine> airLines = (List<AirLine>) query.list();
					AirLine airLine = null;
					if (airLines != null && airLines.size() == 0) {
						airLine = new AirLine();
						airLine.setName(flightSchedule.getAirLineName());
						session.save(airLine);
					} else {
						airLine = airLines.get(0);
					}
					flightSchedule.setAirLineID(airLine.getID());
					session.getTransaction().commit();

					// save dept airport
					session = HibernateUtils.getSessionFactory()
							.getCurrentSession();
					session.beginTransaction();
					query = session
							.createQuery(" select a  from Airport  a where  a.Name = :Name");
					query.setString("Name", flightSchedule.getDeptAirportName());
					List<Airport> airports = (List<Airport>) query.list();
					Airport deptAirport = null;
					if (airports != null && airports.size() == 0) {
						deptAirport = new Airport();
						deptAirport
								.setName(flightSchedule.getDeptAirportName());
						session.save(deptAirport);
					} else {
						deptAirport = airports.get(0);
					}
					flightSchedule.setDeptAirportID(deptAirport.getID());
					session.getTransaction().commit();

					// save arr airport
					session = HibernateUtils.getSessionFactory()
							.getCurrentSession();
					session.beginTransaction();
					query = session
							.createQuery("  select a from Airport  a where  a.Name = :Name");
					query.setString("Name", flightSchedule.getArrAirportName());
					airports = (List<Airport>) query.list();
					Airport arrAirport = null;
					if (airports != null && airports.size() == 0) {
						arrAirport = new Airport();
						arrAirport.setName(flightSchedule.getArrAirportName());
						session.save(arrAirport);
					} else {
						arrAirport = airports.get(0);
					}
					flightSchedule.setArrAirportID(arrAirport.getID());
					session.getTransaction().commit();

					session = HibernateUtils.getSessionFactory()
							.getCurrentSession();
					session.beginTransaction();
					session.save(flightSchedule);
					session.getTransaction().commit();
				}
		 

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
