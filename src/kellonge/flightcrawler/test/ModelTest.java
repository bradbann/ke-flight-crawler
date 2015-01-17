package kellonge.flightcrawler.test;

import java.math.BigDecimal;

import kellonge.flightcrawler.model.FlightPrice;
import kellonge.flightcrawler.utils.DataAccessObject;
import kellonge.flightcrawler.utils.HibernateUtils;

import org.hibernate.Session;

public class ModelTest {
	public static void main(String[] args) {
		 DataAccessObject dao =new DataAccessObject();
		 FlightPrice price =new FlightPrice();
		 price.setFlightInfoID(1);
		 price.setPrice(BigDecimal.valueOf(232.2d));
		 price.setCabin1("Y");
		 dao.saveOrUpdate(price);
		 dao.closeSession();
		 
	}
}
