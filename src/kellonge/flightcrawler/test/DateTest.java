package kellonge.flightcrawler.test;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;

import javassist.expr.NewArray;
import kellonge.flightcrawler.config.Configuration;
import kellonge.flightcrawler.utils.DateTimeUtils;
import kellonge.flightcrawler.utils.Utility;

public class DateTest {
	public static void main(String[] args) {
		Configuration.init();
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				System.out.println(Configuration.getProxyPoolItemRetry());

			}
		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(timerTask, 0, 3000);
	}
}
