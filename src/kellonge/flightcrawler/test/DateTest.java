package kellonge.flightcrawler.test;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import javassist.expr.NewArray;

public class DateTest {
	public static void main(String[] args) {
		Time time = Time.valueOf("19:00");

		System.out.println(time);
		System.out.println(time.getTime());
		Date date = new Date(time.getTime());
		DateFormat df = DateFormat.getInstance();

		System.out.println(df.format(date));

		try {
			Date date2 = DateFormat.getTimeInstance().parse("19:00");			
			Time time2 = new Time(date2.getTime());
			System.out.println(date2);
			System.out.println(time2); 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
