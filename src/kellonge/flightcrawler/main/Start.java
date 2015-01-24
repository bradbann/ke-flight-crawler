package kellonge.flightcrawler.main;

import java.lang.reflect.InvocationTargetException;

import us.codecraft.webmagic.Spider;
import kellonge.flightcrawler.config.Configuration;

public class Start {
	public static void main(String[] args) {
		Configuration.init();
		try {
			Spider spider = (Spider) Class
					.forName(
							"kellonge.flightcrawler.spider."+Configuration.getSpiderName())
					.getMethod("GetSpider").invoke(null);
			spider.start();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		} catch (SecurityException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
	}
}
