package kellonge.flightcrawler.model;

import java.sql.Time; 
import java.util.Date;

import kellonge.flightcrawler.utils.DateTimeUtils;

public class FlightSchedule {

	private int ID;
	private String AirLineName;
	private int AirLineID;
	private String DeptAirportName;
	private int DeptAirportID;
	private Time DeptTime;
	private String DeptCityName;
	private int DeptCityID;
	private String ArrAirportName;
	private int ArrAirportID;
	private Time ArrTime;
	private int ArrCityID;
	private String ArrCityName;
	private Time FlightInterval;
	private String FlightNo;
	private String PlanModel;
	private Date CreateDate;
	private Date ExpiredDate;
	private String DataSource;
	private String ArrTerminal;
	private String DeptTerminal;
	private boolean IsStopOver;
	private String WeekSchedule;	
	private int Flag;

	@Override
	public String toString() {
		return String.format("%s(%s)  %s(%s)--%s(%s)", FlightNo, getAirLineName(),
				DeptAirportName, DateTimeUtils.format(DeptTime), ArrAirportName,
				DateTimeUtils.format(ArrTime));
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getAirLineName() {
		return AirLineName;
	}

	public void setAirLineName(String airLineName) {
		AirLineName = airLineName;
	}

	public int getAirLineID() {
		return AirLineID;
	}

	public void setAirLineID(int airLineID) {
		AirLineID = airLineID;
	}

	public String getDeptAirportName() {
		return DeptAirportName;
	}

	public void setDeptAirportName(String deptAirportName) {
		DeptAirportName = deptAirportName;
	}

	public int getDeptAirportID() {
		return DeptAirportID;
	}

	public void setDeptAirportID(int deptAirportID) {
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

	public int getArrAirportID() {
		return ArrAirportID;
	}

	public void setArrAirportID(int arrAirportID) {
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
 

}
