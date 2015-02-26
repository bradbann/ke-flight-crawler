package kellonge.flightcrawler.model;

import java.sql.Time;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.mysql.jdbc.TimeUtil;

import kellonge.flightcrawler.utils.DateTimeUtils;

public class FlightSchedule {

	private String ID;
	private String AirLineName;
	private String AirLineID;
	private String DeptAirportName;
	private String DeptAirportID;
	private Time DeptTime;
	private String DeptCityName;
	private int DeptCityID;
	private String ArrAirportName;
	private String ArrAirportID;
	private Time ArrTime;
	private int ArrCityID;
	private String ArrCityName;
	private Time FlightInterval;
	private String FlightNo;
	private String PlanModel;
	private Date CreateDate;
	private Date LastUpdate;
	private Date ExpiredDate;
	private String DataSource;
	private String ArrTerminal;
	private String DeptTerminal;
	private boolean IsStopOver;
	private String WeekSchedule;
	private int Flag; 
	private String SpiderID;
	private int NewFlag;

	@Override
	public String toString() {
		return String.format("%s(%s)  %s(%s)--%s(%s)", FlightNo,
				getAirLineName(), DeptAirportName,
				DateTimeUtils.format(DeptTime), ArrAirportName,
				DateTimeUtils.format(ArrTime));
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getAirLineName() {
		return AirLineName;
	}

	public void setAirLineName(String airLineName) {
		AirLineName = airLineName;
	}

	public String getAirLineID() {
		return AirLineID;
	}

	public void setAirLineID(String airLineID) {
		AirLineID = airLineID;
	}

	public String getDeptAirportName() {
		return DeptAirportName;
	}

	public void setDeptAirportName(String deptAirportName) {
		DeptAirportName = deptAirportName;
	}

	public String getDeptAirportID() {
		return DeptAirportID;
	}

	public void setDeptAirportID(String deptAirportID) {
		DeptAirportID = deptAirportID;
	}

	public Time getDeptTime() {
		return DeptTime;
	}

	public void setDeptTime(Time deptTime) {
		DeptTime = deptTime;
	}

	public String getDeptCityName() {
		return DeptCityName;
	}

	public void setDeptCityName(String deptCityName) {
		DeptCityName = deptCityName;
	}

	public int getDeptCityID() {
		return DeptCityID;
	}

	public void setDeptCityID(int deptCityID) {
		DeptCityID = deptCityID;
	}

	public String getArrAirportName() {
		return ArrAirportName;
	}

	public void setArrAirportName(String arrAirportName) {
		ArrAirportName = arrAirportName;
	}

	public String getArrAirportID() {
		return ArrAirportID;
	}

	public void setArrAirportID(String arrAirportID) {
		ArrAirportID = arrAirportID;
	}

	public Time getArrTime() {
		return ArrTime;
	}

	public void setArrTime(Time arrTime) {
		ArrTime = arrTime;
	}

	public int getArrCityID() {
		return ArrCityID;
	}

	public void setArrCityID(int arrCityID) {
		ArrCityID = arrCityID;
	}

	public String getArrCityName() {
		return ArrCityName;
	}

	public void setArrCityName(String arrCityName) {
		ArrCityName = arrCityName;
	}

	public Time getFlightInterval() {
		return FlightInterval;
	}

	public void setFlightInterval(Time flightInterval) {
		FlightInterval = flightInterval;
	}

	public String getFlightNo() {
		return FlightNo;
	}

	public void setFlightNo(String flightNo) {
		FlightNo = flightNo;
	}

	public String getPlanModel() {
		return PlanModel;
	}

	public void setPlanModel(String planModel) {
		PlanModel = planModel;
	}

	public Date getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(Date createDate) {
		CreateDate = createDate;
	}

	public Date getExpiredDate() {
		return ExpiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		ExpiredDate = expiredDate;
	}

	public int getFlag() {
		return Flag;
	}

	public void setFlag(int flag) {
		Flag = flag;
	}

	public String getDataSource() {
		return DataSource;
	}

	public void setDataSource(String dataSource) {
		DataSource = dataSource;
	}

	public String getArrTerminal() {
		return ArrTerminal;
	}

	public void setArrTerminal(String arrTerminal) {
		ArrTerminal = arrTerminal;
	}

	public String getDeptTerminal() {
		return DeptTerminal;
	}

	public void setDeptTerminal(String deptTerminal) {
		DeptTerminal = deptTerminal;
	}

	public boolean isIsStopOver() {
		return IsStopOver;
	}

	public void setIsStopOver(boolean isStopOver) {
		IsStopOver = isStopOver;
	}

	public String getWeekSchedule() {
		return WeekSchedule;
	}

	public void setWeekSchedule(String weekSchedule) {
		WeekSchedule = weekSchedule;
	}

	public Date getLastUpdate() {
		return LastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		LastUpdate = lastUpdate;
	}

 

	@Override
	public boolean equals(Object obj) {
		FlightSchedule o = (FlightSchedule) obj;
		if (!StringUtils.equals(AirLineID, o.AirLineID)) {
			return false;
		}
		if (!StringUtils.equals(DeptAirportID, o.DeptAirportID)) {
			return false;
		}
		if (!StringUtils.equals(ArrAirportID, o.ArrAirportID)) {
			return false;
		}
		if (DeptCityID != o.DeptCityID) {
			return false;
		}
		if (ArrCityID != o.ArrCityID) {
			return false;
		}

		if (!StringUtils.equals(FlightNo, o.FlightNo)) {
			return false;
		}
		if (!StringUtils.equals(PlanModel, o.PlanModel)) {
			return false;
		}

		if (!DeptTime.equals(o.DeptTime)) {
			return false;
		}
		if (!ArrTime.equals(o.ArrTime)) {
			return false;
		}

		if (!StringUtils.equals(DeptTerminal, o.DeptTerminal)) {
			return false;
		}

		if (!StringUtils.equals(ArrTerminal, o.ArrTerminal)) {
			return false;
		}
		if (IsStopOver != o.IsStopOver) {
			return false;
		}

		if (!StringUtils.equals(WeekSchedule, o.WeekSchedule)) {
			return false;
		}
		return true;
	}

	public String getSpiderID() {
		return SpiderID;
	}

	public void setSpiderID(String spiderID) {
		SpiderID = spiderID;
	}

	public int getNewFlag() {
		return NewFlag;
	}

	public void setNewFlag(int newFlag) {
		NewFlag = newFlag;
	}

}
