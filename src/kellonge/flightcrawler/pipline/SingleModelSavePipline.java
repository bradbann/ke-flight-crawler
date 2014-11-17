package kellonge.flightcrawler.pipline;

import java.util.List;

import org.hibernate.Session;

import kellonge.flightcrawler.utils.HibernateUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author kellonge 处理单个实体保存到数据库
 * @param <T>
 *            实体类型
 */
public class SingleModelSavePipline<T> implements Pipeline {

	@Override
	public void process(ResultItems resultItems, Task task) {
		try {
			List<T> modelData = (List<T>) resultItems.get("ModelData");
			for (T t : modelData) {
				Session session = HibernateUtils.getSessionFactory()
						.getCurrentSession();
				session.beginTransaction();
				session.save(t);
				session.getTransaction().commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
