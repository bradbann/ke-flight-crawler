package kellonge.flightcrawler.model;

public class City {
	private int ID;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}
	public String getCityNameEn() {
		return CityNameEn;
	}
	public void setCityNameEn(String cityNameEn) {
		CityNameEn = cityNameEn;
	}
	public String getCityNamePY() {
		return CityNamePY;
	}
	public void setCityNamePY(String cityNamePY) {
		CityNamePY = cityNamePY;
	}
	public String getCityCode1() {
		return CityCode1;
	}
	public void setCityCode1(String cityCode1) {
		CityCode1 = cityCode1;
	}
	public boolean isHasAirport() {
		return HasAirport;
	}
	public void setHasAirport(boolean hasAirport) {
		HasAirport = hasAirport;
	}
	private String CityName;
	private String CityNameEn;
	private String CityNamePY;
	private String CityCode1;
	private boolean HasAirport;
}
