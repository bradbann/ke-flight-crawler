package kellonge.flightcrawler.model.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.utils.DataAccessObject;
import kellonge.flightcrawler.utils.DateTimeUtils;
import kellonge.flightcrawler.utils.Utility;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FlightScheduleManager {

	DataAccessObject dao = new DataAccessObject();
	Logger logger = Logger.getLogger(this.getClass());

	public FlightScheduleManager() {
	}

	public void saveFlightSchedule(FlightSchedule flightSchedule) {
		List<FlightSchedule> list = getFlightSchedules(
				flightSchedule.getFlightNo(),
				flightSchedule.getDeptAirportID(),
				flightSchedule.getArrAirportID(),
				flightSchedule.getWeekSchedule());
		if (list.size() > 0) {
			FlightSchedule flightScheduleOld = list.get(0);
			if (!flightSchedule.equals(flightScheduleOld)) {
				FlightSchedule flightScheduleHistory = new FlightSchedule();
				try {
					BeanUtils.copyProperties(flightScheduleHistory,
							flightScheduleOld);
					flightScheduleHistory.setID("");
					flightScheduleHistory.setFlag(-1);
					dao.saveOrUpdate(flightScheduleHistory);
					String id = flightScheduleOld.getID();
					Date createDate = flightScheduleOld.getCreateDate();
					BeanUtils.copyProperties(flightScheduleOld, flightSchedule);
					flightScheduleOld.setID(id);
					flightScheduleOld.setCreateDate(createDate);
					dao.saveOrUpdate(flightScheduleOld);
				} catch (Exception err) {
					logger.info("savrFlightSchedule" + err.getMessage());
				} finally {
					DataAccessObject.closeSession();
				}
			}

		} else {
			try {
				flightSchedule.setCreateDate(new Date());
				dao.saveOrUpdate(flightSchedule);
			} catch (Exception err) {
				logger.info("savrFlightSchedule" + err.getMessage());
			} finally {
				DataAccessObject.closeSession();
			}
		}
	}

	public List<FlightSchedule> getFlightSchedules(String strFlightNo,
			String strDeptAirportID, String strArrAirportID,
			String strWeekSchedule) {
		List<FlightSchedule> flightSchedules = new ArrayList<FlightSchedule>();
		try {
			Query query = getFlightSchedulesQuery("a", strFlightNo,
					strDeptAirportID, strArrAirportID, strWeekSchedule);
			flightSchedules = query.list();
			for (FlightSchedule flightSchedule : flightSchedules) {
				extendFlightSchedule(flightSchedule);
			}
		} catch (Exception e) {
			logger.info("getFlightSchedules" + e.getMessage());
		} finally {
			DataAccessObject.closeSession();
		}

		return flightSchedules;
	}

	private Query getFlightSchedulesQuery(String strChange, String strFlightNo,
			String strDeptAirportID, String strArrAirportID,
			String strWeekSchedule) {
		String hql = "SELECT " + strChange
				+ " FROM FlightSchedule a  WHERE Flag=1";
		if (StringUtils.isNotEmpty(strFlightNo)) {
			hql += " AND FlightNo = :FlightNo";
		}
		if (StringUtils.isNotEmpty(strDeptAirportID)) {
			hql += " AND DeptAirportID =:DeptAirportID";
		}
		if (StringUtils.isNotEmpty(strArrAirportID)) {
			hql += " AND ArrAirportID =:ArrAirportID";
		}
		if (StringUtils.isNotEmpty(strWeekSchedule)) {
			hql += " AND WeekSchedule= :WeekSchedule";
		}
		Query query = DataAccessObject.openSession().createQuery(hql);
		if (StringUtils.isNotEmpty(strFlightNo)) {
			query.setString("FlightNo", strFlightNo);
		}
		if (StringUtils.isNotEmpty(strDeptAirportID)) {
			query.setString("DeptAirportID", strDeptAirportID);
		}
		if (StringUtils.isNotEmpty(strArrAirportID)) {
			query.setString("ArrAirportID", strArrAirportID);

		}
		if (StringUtils.isNotEmpty(strWeekSchedule)) {
			query.setString("WeekSchedule", strWeekSchedule);
		}
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

	// public boolean removeFlightSchedules(String removeIDs) {
	// if (StringUtils.isEmpty(removeIDs)) {
	// return false;
	// }
	//
	// Transaction transaction = null;
	// Session session = null;
	// try {
	// session = DataAccessObject.openSession();
	// transaction = session.beginTransaction();
	//
	// String[] arrMemberIDs = Utility.getStrArray(removeIDs);
	// for (Object id : arrMemberIDs) {
	// this.removeFlightScheduleByID(session, Utility.toSafeString(id));
	// }
	// transaction.commit();
	// return true;
	// } catch (Exception e) {
	// transaction.rollback();
	// logger.info(e.getMessage());
	// return false;
	// } finally {
	// DataAccessObject.closeSession();
	// }
	// }
	//
	// private void removeFlightScheduleByID(Session session, String strID) {
	// dao.remove(session, FlightSchedule.class, strID);
	// }
}
