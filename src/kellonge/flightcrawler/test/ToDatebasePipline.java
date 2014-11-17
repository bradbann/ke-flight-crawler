package kellonge.flightcrawler.test;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.model.FlightPrice;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class ToDatebasePipline implements Pipeline {

	@Override
	public void process(ResultItems resultItems, Task task) {

		Map<FlightSchedule, List<FlightPrice>> FlightDatas = (Map<FlightSchedule, List<FlightPrice>>) resultItems
				.get("FlightDatas");
	 

		for (Entry<FlightSchedule, List<FlightPrice>> item : FlightDatas.entrySet()) {
			Session session = MainTest1.sessionFactory.getCurrentSession();
			session.beginTransaction();
			session.save(item.getKey());
			session.getTransaction().commit();
		}

		System.out.println("save success");
	}

}
