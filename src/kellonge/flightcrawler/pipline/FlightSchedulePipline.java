package kellonge.flightcrawler.pipline;

import java.util.List;

import org.hibernate.Session;

import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.utils.HibernateUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author kellonge 处理航班时刻保存
 * @param <T>
 *            实体类型
 */
public class FlightSchedulePipline implements Pipeline {

	@Override
	public void process(ResultItems resultItems, Task task) {
		try {
			List<FlightSchedule> modelData = (List<FlightSchedule>) resultItems
					.get("ModelData");
			if (modelData != null) {
				for (FlightSchedule flightSchedule : modelData) {
					 
					
					Session session = HibernateUtils.getSessionFactory()
							.getCurrentSession();
					session.beginTransaction();
					session.save(flightSchedule);
					session.getTransaction().commit();

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
