package kellonge.flightcrawler.model.manager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kellonge.flightcrawler.model.City;
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

	public void updateFlightScheduleStatusBeforeFetch() {
		try {
			Session session = DataAccessObject.openSession();
			session.beginTransaction();
			Query query = session
					.createQuery(" update FlightSchedule a  set a.NewFlag=1 where a.Flag=1 and a.NewFlag!=3");
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.info("updateFlightScheduleStatusBeforeFetch"
					+ e.getMessage());
		} finally {
			DataAccessObject.closeSession();
		}

	}

	public void updateFlightScheduleStatusAfterFetch() {
		try {
			Session session = DataAccessObject.openSession();
			session.beginTransaction();
			Query query = session
					.createQuery(" update FlightSchedule a  set a.NewFlag=3 where  a.Flag=1 and  a.NewFlag=1 ");
			query.executeUpdate();
			query = session
					.createQuery(" update FlightSchedule a  set a.NewFlag=0 where a.Flag=1 and  a.NewFlag=2 ");
			query.executeUpdate();
			session.getTransaction().commit();

		} catch (Exception e) {
			logger.info("updateFlightScheduleStatusBeforeFetch"
					+ e.getMessage());
		} finally {
			DataAccessObject.closeSession();
		}
	}

	/**
	 * save info when exist old recorder,so make old recorder to history
	 * 
	 * @param newflightSchedule
	 * @param flightScheduleOld
	 */
	public void saveFlightScheduleWithHistory(FlightSchedule newflightSchedule,
			FlightSchedule flightScheduleOld) {
		try {
			FlightSchedule flightScheduleHistory = new FlightSchedule();

			BeanUtils.copyProperties(flightScheduleHistory, flightScheduleOld);
			flightScheduleHistory.setFlag(-1);
			flightScheduleHistory.setID(null);
			dao.saveOrUpdate(flightScheduleHistory);
			String id = flightScheduleOld.getID();
			Date createDate = flightScheduleOld.getCreateDate();
			BeanUtils.copyProperties(flightScheduleOld, newflightSchedule);
			flightScheduleOld.setID(id);
			flightScheduleOld.setCreateDate(createDate);
			flightScheduleOld.setNewFlag(2);
			dao.saveOrUpdate(flightScheduleOld);
		} catch (Exception err) {
			logger.info("saveFlightScheduleWithHistory" + err.getMessage());
		} finally {
			DataAccessObject.closeSession();
		}
	}

	public void saveFlightSchedule(FlightSchedule flightSchedule) {
		try {
			List<FlightSchedule> list = getFlightSchedules(
					flightSchedule.getFlightNo(),
					flightSchedule.getDeptAirportID(),
					flightSchedule.getArrAirportID(), "");

			// edit
			if (list.size() == 1) {
				FlightSchedule flightScheduleOld = list.get(0);
				if (!flightSchedule.equals(flightScheduleOld)) {
					// edit to insert new
					if (flightScheduleOld.getWeekSchedule().equals(
							flightSchedule.getWeekSchedule())) {
						saveFlightScheduleWithHistory(flightSchedule,
								flightScheduleOld);
					} else {
						// add new
						flightSchedule.setCreateDate(new Date());
						flightSchedule.setNewFlag(2);
						dao.saveOrUpdate(flightSchedule);
					}
				} else {
					// edit to update old
					flightScheduleOld.setSpiderID(flightSchedule.getSpiderID());
					flightScheduleOld.setNewFlag(2);
					dao.saveOrUpdate(flightScheduleOld);
				}
			}
			if (list.size() > 1) {
				// add new
				flightSchedule.setCreateDate(new Date());
				flightSchedule.setNewFlag(2);
				dao.saveOrUpdate(flightSchedule);
				// update price reference
				String ids = "";
				for (int i = 0; i < list.size(); i++) {
					if (i == 0) {
						ids += "'" + list.get(i).getID() + "'";
					} else {
						ids += ",'" + list.get(i).getID() + "'";
					}
				}
				new FlightInfoManager().updateFlightInfoReferenceByParam(
						flightSchedule.getID(),
						flightSchedule.getWeekSchedule(), ids);
			}

			// add
			if (list.size() == 0) {
				flightSchedule.setCreateDate(new Date());
				flightSchedule.setNewFlag(2);
				dao.saveOrUpdate(flightSchedule);
			}
		} catch (Exception err) {
			logger.info("savrFlightSchedule" + err.getMessage());
		} finally {
			DataAccessObject.closeSession();
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

	public FlightSchedule getFlightScheduleByParam(String strFlightNo,
			String strDeptCityCode, String strArrCityCode, Date flightDate) {
		List<FlightSchedule> flightSchedules = getFlightSchedules(strFlightNo,
				"", "", "");
		for (FlightSchedule flightSchedule : flightSchedules) {
			City deptCity = new CityManager().getCityByName(flightSchedule
					.getDeptCityName());
			City arrCity = new CityManager().getCityByName(flightSchedule
					.getArrCityName());
			if (deptCity != null && arrCity != null
					&& flightSchedule.getDeptCityID() == deptCity.getID()
					&& flightSchedule.getArrCityID() == arrCity.getID()) {
				if (IsTodayFlight(flightSchedule, flightDate)) {
					return flightSchedule;
				}
			}

		}
		return null;
	}

	public static boolean IsTodayFlight(FlightSchedule flightSchedule, Date date) {
		String[] weekScheduleStrings = flightSchedule.getWeekSchedule().split(
				",");
		int week = DateTimeUtils.getWeekOfDate(date);
		if (weekScheduleStrings.length >= week) {
			if ("1".equals(weekScheduleStrings[week - 1])) {
				return true;
			}
		}
		return false;
	}

	public List<FlightSchedule> getNoPriceFlightSchedules(){
		List<FlightSchedule> flightSchedules = new ArrayList<FlightSchedule>();
		try {
			String hql=" select fs  from FlightSchedule fs where fs.Flag=1 and not exists(select 1 from FlightInfo fi where fi.FlightScheduleID=fs.ID)   ";
			Query query = DataAccessObject.openSession().createQuery(hql);
			flightSchedules = query.list();
			for (FlightSchedule flightSchedule : flightSchedules) {
				extendFlightSchedule(flightSchedule);
			}
		} catch (Exception e) {
			logger.info("getNoPriceFlightSchedules" + e.getMessage());
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
