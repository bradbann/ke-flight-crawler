package kellonge.flightcrawler.model.manager;

import java.util.ArrayList;
import java.util.List;

import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.utils.DataAccessObject;
import kellonge.flightcrawler.utils.Utility;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FlightScheduleManager {

	DataAccessObject dao = new DataAccessObject();
	Logger logger = Logger.getLogger(this.getClass());

	private List<FlightSchedule> flightSchedules = null;

	public FlightScheduleManager() {
	}

	public void saveFlightSchedule(FlightSchedule flightSchedule) { 
		try { 
			dao.saveOrUpdate(flightSchedule);
		} catch (Exception err) {
			logger.info("savrFlightSchedule" + err.getMessage());
		} finally {
			DataAccessObject.closeSession();
		}
	}

 

	public List<FlightSchedule> getFlightSchedules() {
		if (flightSchedules != null) {
			return flightSchedules;
		}
		List<FlightSchedule> flightSchedules = new ArrayList<FlightSchedule>();
		try {
			Query query = getFlightSchedulesQuery("a");
			flightSchedules = query.list();
			for (FlightSchedule flightSchedule : flightSchedules) {
				extendFlightSchedule(flightSchedule);
			}
			flightSchedules = flightSchedules;
		} catch (Exception e) {
			logger.info("getFlightSchedules" + e.getMessage());
		} finally {
			DataAccessObject.closeSession();
		}

		return flightSchedules;
	}

	private Query getFlightSchedulesQuery(String strChange) {
		String hql = "SELECT " + strChange + " FROM FlightSchedule a  ";
		Query query = DataAccessObject.openSession().createQuery(hql);
		return query;
	}

	private FlightSchedule extendFlightSchedule(FlightSchedule flightSchedule) {
		try {
			if (flightSchedule != null) {

			}

		} catch (Exception e) {
			logger.info("extendFlightSchedule" + e.getMessage());
		}
		return flightSchedule;
	}

	public boolean removeFlightSchedules(String removeIDs) {
		if (StringUtils.isEmpty(removeIDs)) {
			return false;
		}

		Transaction transaction = null;
		Session session = null;
		try {
			session = DataAccessObject.openSession();
			transaction = session.beginTransaction();

			String[] arrMemberIDs = Utility.getStrArray(removeIDs);
			for (Object id : arrMemberIDs) {
				this.removeFlightScheduleByID(session, Utility.toSafeString(id));
			}
			transaction.commit();
			return true;
		} catch (Exception e) {
			transaction.rollback();
			logger.info(e.getMessage());
			return false;
		} finally {
			DataAccessObject.closeSession();
		}
	}

	private void removeFlightScheduleByID(Session session, String strID) {
		dao.remove(session, FlightSchedule.class, strID);
	}
}
