package kellonge.flightcrawler.model.manager;

import java.util.ArrayList;
import java.util.List;

import kellonge.flightcrawler.model.FlightPrice;
import kellonge.flightcrawler.utils.DataAccessObject;
import kellonge.flightcrawler.utils.Utility;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FlightPriceManager {

	DataAccessObject dao = new DataAccessObject();
	Logger logger = Logger.getLogger(this.getClass());

	public FlightPriceManager() {
	}

	public void saveFlightPrice(FlightPrice flightPrice) {
		try {
			dao.saveOrUpdate(flightPrice);
		} catch (Exception err) {
			logger.info("savrFlightPrice" + err.getMessage());
		} finally {
			DataAccessObject.closeSession();
		}
	}

	public List<FlightPrice> getFlightPrices() {
		List<FlightPrice> flightPrices = new ArrayList<FlightPrice>();
		try {
			Query query = getFlightPricesQuery("f");
			flightPrices = query.list();
			for (FlightPrice flightPrice : flightPrices) {
				extendFlightPrice(flightPrice);
			}
		} catch (Exception e) {
			logger.info("getFlightPrices" + e.getMessage());
		} finally {
			DataAccessObject.closeSession();
		}

		return flightPrices;
	}

	private Query getFlightPricesQuery(String strChange) {
		String hql = "SELECT " + strChange + " FROM FlightPrice f  ";
		Query query = DataAccessObject.openSession().createQuery(hql);
		return query;
	}

	private FlightPrice extendFlightPrice(FlightPrice flightPrice) {
		try {
			if (flightPrice != null) {

			}

		} catch (Exception e) {
			logger.info("extendFlightPrice" + e.getMessage());
		}
		return flightPrice;
	}

	public boolean removeFlightPrices(String removeIDs) {
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
				this.removeFlightPriceByID(session, Utility.toSafeString(id));
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

	private void removeFlightPriceByID(Session session, String strID) {
		dao.remove(session, FlightPrice.class, strID);
	}
}
