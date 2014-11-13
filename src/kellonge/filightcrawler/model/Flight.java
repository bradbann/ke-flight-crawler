package kellonge.filightcrawler.model;

import java.text.DateFormat;
import java.util.Date;

import kellonge.filightcrawler.utils.DateUtils;

public class Flight {

	private String FlightNo;
	private String AirLineName;
	private String PlanModel;
	private String DeptAirportName;
	private Date DeptTime;
	private String ArrAirportName;
	private Date ArrTime;

	public String getFlightNo() {
		return FlightNo;
	}

	public void setFlightNo(String flightNo) {
		FlightNo = flightNo;
	}

	public String getAirLineName() {
		return AirLineName;
	}

	public void setAirLineName(String airLineName) {
		AirLineName = airLineName;
	}

	public String getPlanModel() {
		return PlanModel;
	}

	public void setPlanModel(String planModel) {
		PlanModel = planModel;
	}

	public String getDeptAirportName() {
		return DeptAirportName;
	}

	public void setDeptAirportName(String deptAirportName) {
		DeptAirportName = deptAirportName;
	}

	public Date getDeptTime() {
		return DeptTime;
	}

	public void setDeptTime(Date deptTime) {
		DeptTime = deptTime;
	}

	public String getArrAirportName() {
		return ArrAirportName;
	}

	public void setArrAirportName(String arrAirportName) {
		ArrAirportName = arrAirportName;
	}

	public Date getArrTime() {
		return ArrTime;
	}

	public void setArrTime(Date arrTime) {
		ArrTime = arrTime;
	}

	@Override
	public String toString() {
		return String.format("%s(%s)  %s(%s)--%s(%s)", FlightNo, AirLineName,
				DeptAirportName, DateUtils.format(DeptTime), ArrAirportName,
				DateUtils.format(ArrTime));
	}

}
