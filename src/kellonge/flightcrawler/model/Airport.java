package kellonge.flightcrawler.model;

public class Airport {
	private String ID;
	private String Name;
	private String NameEn;
	private String NamePY;
	private String Code;
	private int CityID;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getNameEn() {
		return NameEn;
	}

	public void setNameEn(String nameEn) {
		NameEn = nameEn;
	}

	public String getNamePY() {
		return NamePY;
	}

	public void setNamePY(String namePY) {
		NamePY = namePY;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public int getCityID() {
		return CityID;
	}

	public void setCityID(int cityID) {
		CityID = cityID;
	}
}
