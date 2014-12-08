package kellonge.flightcrawler.model;

import java.math.BigDecimal;

public class FlightPrice {

	private int ID;
	private int FlightInfoID;
	private BigDecimal Price;
	private String Cabin1;
	private String Cabin2;
	private String Cabin3;
	private String Remark;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getFlightInfoID() {
		return FlightInfoID;
	}

	public void setFlightInfoID(int flightInfoID) {
		FlightInfoID = flightInfoID;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal price) {
		Price = price;
	}

	public String getCabin1() {
		return Cabin1;
	}

	public void setCabin1(String cabin1) {
		Cabin1 = cabin1;
	}

	public String getCabin2() {
		return Cabin2;
	}

	public void setCabin2(String cabin2) {
		Cabin2 = cabin2;
	}

	public String getCabin3() {
		return Cabin3;
	}

	public void setCabin3(String cabin3) {
		Cabin3 = cabin3;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

}
