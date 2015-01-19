package kellonge.flightcrawler.model.manager;

import java.util.ArrayList;
import java.util.List;

import kellonge.flightcrawler.model.AirLine;
import kellonge.flightcrawler.utils.DataAccessObject;
import kellonge.flightcrawler.utils.Utility;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AirLineManager {

	DataAccessObject dao = new DataAccessObject();
	Logger logger = Logger.getLogger(this.getClass());

	private static List<AirLine> airLines = null;

	public AirLineManager() {
	}

	public void saveAirLine(AirLine airLine) {
		try {
			dao.saveOrUpdate(airLine); 
			airLines = null;
		} catch (Exception err) {
			logger.info("savrAirLine" + err.getMessage());
		} finally {
			DataAccessObject.closeSession();
		}
	}

	public AirLine getAirLineByName(String name) {
		List<AirLine> airLines = getAirLines();
		for (AirLine airLine : airLines) {
			if (airLine.getName().equals(name)) {
				return airLine;
			}
		}
		return null;
	}

	public List<AirLine> getAirLines() {
		if (airLines == null) {
			try {
				Query query = getAirLinesQuery("a");
				airLines = query.list();
				for (AirLine airLine : airLines) {
					extendAirLine(airLine);
				}
			} catch (Exception e) {
				logger.info("getAirLines" + e.getMessage());
			} finally {
				DataAccessObject.closeSession();
			}
		}

		return airLines;
	}

	private Query getAirLinesQuery(String strChange) {
		String hql = "SELECT " + strChange + " FROM AirLine a  ";
		Query query = DataAccessObject.openSession().createQuery(hql);
		return query;
	}

	private AirLine extendAirLine(AirLine airLine) {
		try {
			if (airLine != null) {

			}

		} catch (Exception e) {
			logger.info("extendAirLine" + e.getMessage());
		}
		return airLine;
	}

	public boolean removeAirLines(String removeIDs) {
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
				this.removeAirLineByID(session, Utility.toSafeString(id));
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

	private void removeAirLineByID(Session session, String strID) {
		dao.remove(session, AirLine.class, strID);
	}
}
