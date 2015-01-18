package kellonge.flightcrawler.model.manager;

import java.util.ArrayList;
import java.util.List;

import kellonge.flightcrawler.model.City;
import kellonge.flightcrawler.utils.DataAccessObject;
import kellonge.flightcrawler.utils.Utility;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CityManager {

	DataAccessObject dao = new DataAccessObject();
	Logger logger = Logger.getLogger(this.getClass());

	private List<City> cities = null;

	public CityManager() {
	}

	public void saveCity(int nTypeID, String strDataID) {
		City city = null;
		try {
			city = new City();
			dao.saveOrUpdate(city);
		} catch (Exception err) {
			logger.info("savrCity" + err.getMessage());
		} finally {
			DataAccessObject.closeSession();
		}
	}

	public City getCityByName(String name) {
		List<City> cities = getCitys();
		for (City city : cities) {
			if (city.getCityName().equals(name)) {
				return city;
			}
		}
		return null;
	}

	public List<City> getCitys() {
		if (cities != null) {
			return cities;
		}
		List<City> citys = new ArrayList<City>();
		try {
			Query query = getCitysQuery("c");
			citys = query.list();
			for (City city : citys) {
				extendCity(city);
			}
			cities = citys;
		} catch (Exception e) {
			logger.info("getCitys" + e.getMessage());
		} finally {
			DataAccessObject.closeSession();
		}

		return cities;
	}

	private Query getCitysQuery(String strChange) {
		String hql = "SELECT " + strChange + " FROM City c  ";
		Query query = DataAccessObject.openSession().createQuery(hql);
		return query;
	}

	private City extendCity(City city) {
		try {
			if (city != null) {

			}

		} catch (Exception e) {
			logger.info("extendCity" + e.getMessage());
		}
		return city;
	}

	public boolean removeCitys(String removeIDs) {
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
				this.removeCityByID(session, Utility.toSafeString(id));
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

	private void removeCityByID(Session session, String strID) {
		dao.remove(session, City.class, strID);
	}
}
