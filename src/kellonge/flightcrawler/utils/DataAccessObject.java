package kellonge.flightcrawler.utils;

import java.io.File;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DataAccessObject {

	public DataAccessObject() {
	}

	public void saveOrUpdate(Object object) {
		Transaction transaction = openSession().beginTransaction();
		try {
			openSession().saveOrUpdate(object);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}
		closeSession();
	}

	public static Session openSession() throws HibernateException {
		Session session = (Session) threadLocal.get();
		if (session == null || !session.isOpen()) {
			if (sessionFactory == null)
				rebuildSessionFactory();
			session = sessionFactory == null ? null : sessionFactory
					.openSession();
			threadLocal.set(session);
		}
		return session;
	}

	public static void closeSession() throws HibernateException {
		Session session = (Session) threadLocal.get();
		threadLocal.set(null);
		if (session != null)
			session.close();
	}

	public static void rebuildSessionFactory() {
		try {
			if (configuration==null) {
				configuration=new Configuration();
			}
			configuration .configure(new File(kellonge.flightcrawler.config.Configuration.getHibernateConfig()));
			StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
			sb.applySettings(configuration.getProperties());
			StandardServiceRegistry standardServiceRegistry = sb.build();
			sessionFactory = configuration
					.buildSessionFactory(standardServiceRegistry);
		} catch (Throwable th) {
			System.err.println((new StringBuilder(
					"Enitial SessionFactory creation failed")).append(th)
					.toString());
			throw new ExceptionInInitializerError(th);
		}
	}

	static {
		try {
			rebuildSessionFactory();
		} catch (Exception exception) {
		}
	}

	private static final ThreadLocal threadLocal = new ThreadLocal();
	private static Configuration configuration = new Configuration();
	private static SessionFactory sessionFactory;

}
