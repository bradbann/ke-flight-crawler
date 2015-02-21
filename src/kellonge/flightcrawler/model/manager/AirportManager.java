package kellonge.flightcrawler.model.manager;

import java.util.ArrayList;
import java.util.List;

import kellonge.flightcrawler.model.Airport;
import kellonge.flightcrawler.utils.DataAccessObject;
import kellonge.flightcrawler.utils.Utility;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AirportManager {

	DataAccessObject dao = new DataAccessObject();
	Logger logger = Logger.getLogger(this.getClass());

	private static List<Airport> airports = null;

	public AirportManager() {
	}

	public void saveAirport(Airport airport) {
		try {
			dao.saveOrUpdate(airport);
			airports = null;
		} catch (Exception err) {
			logger.info("savrAirport" + err.getMessage());
		} finally {
			DataAccessObject.closeSession();
		}
	}

	public Airport getAirportByName(String name) {
		List<Airport> airports = getAirports();
		for (Airport airport : airports) {
			if (airport.getName().equals(name)) {
				return airport;
			}
		}
		return null;
	}

	public Airport getAirportByCode(String code) {
		List<Airport> airports = getAirports();
		for (Airport airport : airports) {
			if (airport.getCode().equals(code)) {
				return airport;
			}
		}
		return null;
	}

	public List<Airport> getAirports() {
		if (airports == null) {
			try {
				Query query = getAirportsQuery("a");
				airports = query.list();
				for (Airport airport : airports) {
					extendAirport(airport);
				}
			} catch (Exception e) {
				logger.info("getAirports" + e.getMessage());
			} finally {
				DataAccessObject.closeSession();
			}
		}

		return airports;
	}

	private Query getAirportsQuery(String strChange) {
		String hql = "SELECT " + strChange + " FROM Airport a  ";
		Query query = DataAccessObject.openSession().createQuery(hql);
		return query;
	}

	private Airport extendAirport(Airport airport) {
		try {
			if (airport != null) {

			}

		} catch (Exception e) {
			logger.info("extendAirport" + e.getMessage());
		}
		return airport;
	}

	public boolean removeAirports(String removeIDs) {
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
				this.removeAirportByID(session, Utility.toSafeString(id));
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

	private void removeAirportByID(Session session, String strID) {
		dao.remove(session, Airport.class, strID);
	}
}
