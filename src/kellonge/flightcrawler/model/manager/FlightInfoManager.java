package kellonge.flightcrawler.model.manager;

import java.util.ArrayList;
import java.util.List;

import kellonge.flightcrawler.model.FlightInfo;
import kellonge.flightcrawler.utils.DataAccessObject;
import kellonge.flightcrawler.utils.Utility;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FlightInfoManager {

	DataAccessObject dao = new DataAccessObject();
	Logger logger = Logger.getLogger(this.getClass());

	public FlightInfoManager() {
	}

	public void updateFlightInfoReferenceByParam(String newFlightScheduleID,
			String newWeekSchedule, String oldFlightScheduleID) {
		String[] arrWeekSchedules = newWeekSchedule.split(",");
		if (arrWeekSchedules.length == 7) {
			String where = " FlightScheduleID  IN (" + oldFlightScheduleID
					+ ") AND(   ";
			boolean isFirst = true;
			for (int i = 0; i < arrWeekSchedules.length; i++) {
				if (arrWeekSchedules[i].equals("1")) {
					if (i <= 5) {
						if (isFirst) {
							where += "    DAYOFWEEK(FlightDate)= " + (i + 2);
							isFirst = false;
						} else {
							where += " or  DAYOFWEEK(FlightDate)= " + (i + 2);
						}

					} else {
						if (isFirst) {
							where += "    DAYOFWEEK(FlightDate)= " + 1;
							isFirst = false;
						} else {
							where += " or  DAYOFWEEK(FlightDate)= " + 1;
						}
					}
				}
			}
			where += ") ";
			String sql = "update FlightInfo set FlightScheduleID='"
					+ newFlightScheduleID + "' where  " + where + ";";

			try {
				Session session = DataAccessObject.openSession();
				session.beginTransaction();
				Query query = session.createSQLQuery(sql);
				query.executeUpdate();
				session.getTransaction().commit();

				sql = " update FlightSchedule set flag=-1  where flag=1  ";
				sql += " and ID IN (" + oldFlightScheduleID + ") ";
				sql += " and not exists(select 1 from FlightInfo where flag=1 and FlightInfo.FlightScheduleID=FlightSchedule.ID); ";

				session = DataAccessObject.openSession();
				session.beginTransaction();
				query = session.createSQLQuery(sql);
				query.executeUpdate();
				session.getTransaction().commit();
			} catch (Exception e) {
				logger.info("updateFlightInfoReferenceByParam" + e.getMessage());
			} finally {
				DataAccessObject.closeSession();
			}
		}
	}

	public void saveFlightInfo(FlightInfo flightInfo) {
		try {
			dao.saveOrUpdate(flightInfo);
		} catch (Exception err) {
			logger.info("savrFlightInfo" + err.getMessage());
		} finally {
			DataAccessObject.closeSession();
		}
	}

	public List<FlightInfo> getFlightInfos() {

		List<FlightInfo> flightInfos = new ArrayList<FlightInfo>();
		try {
			Query query = getFlightInfosQuery("a");
			flightInfos = query.list();
			for (FlightInfo flightInfo : flightInfos) {
				extendFlightInfo(flightInfo);
			}
			flightInfos = flightInfos;
		} catch (Exception e) {
			logger.info("getFlightInfos" + e.getMessage());
		} finally {
			DataAccessObject.closeSession();
		}

		return flightInfos;
	}

	private Query getFlightInfosQuery(String strChange) {
		String hql = "SELECT " + strChange + " FROM FlightInfo a  ";
		Query query = DataAccessObject.openSession().createQuery(hql);
		return query;
	}

	private FlightInfo extendFlightInfo(FlightInfo flightInfo) {
		try {
			if (flightInfo != null) {

			}

		} catch (Exception e) {
			logger.info("extendFlightInfo" + e.getMessage());
		}
		return flightInfo;
	}

	public boolean removeFlightInfos(String removeIDs) {
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
				this.removeFlightInfoByID(session, Utility.toSafeString(id));
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

	private void removeFlightInfoByID(Session session, String strID) {
		dao.remove(session, FlightInfo.class, strID);
	}
}
