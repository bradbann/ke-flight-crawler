package kellonge.flightcrawler.test;

import java.math.BigDecimal;

import kellonge.flightcrawler.model.FlightPrice;
import kellonge.flightcrawler.utils.HibernateUtils;

import org.hibernate.Session;

public class ModelTest {
	public static void main(String[] args) {
		Session session = HibernateUtils.getSessionFactory()
				.getCurrentSession();
		session.beginTransaction();
		FlightPrice flightPrice = new FlightPrice();
		flightPrice.setFlightInfoID(11);
		flightPrice.setPrice(BigDecimal.valueOf(1232));
		flightPrice.setCabin1("F");
		session.save(flightPrice);
		session.getTransaction().commit();
	}
}
