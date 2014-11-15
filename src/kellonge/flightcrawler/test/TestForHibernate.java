package kellonge.flightcrawler.test;

import java.sql.Time;
import java.util.Date;

import kellonge.flightcrawler.model.Flight;
import kellonge.flightcrawler.utils.DateTimeUtils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TestForHibernate {

	public static void main(String[] args) {

		 SessionFactory sessionFactory = new Configuration().configure()
		 .buildSessionFactory();
		 Session session = sessionFactory.getCurrentSession();
		 session.beginTransaction();
		 Flight flight = new Flight();
		 flight.setAirLineName("东方航空");
		 flight.setArrAirportName("杭州");
		 flight.setArrCityName("杭州");
		 flight.setArrTime(new Time(22, 00, 00));
		 flight.setDeptAirportName("杭州");
		 flight.setDeptCityName("杭州");
		 flight.setDeptTime(new Time(19, 20, 00));
		 flight.setExpiredDate(new Date(2014, 12, 30));
		 flight.setFlightNo("MU1235");
		 session.save(flight);
		 session.getTransaction().commit();
		 System.out.println("save success");
 

	}
}
