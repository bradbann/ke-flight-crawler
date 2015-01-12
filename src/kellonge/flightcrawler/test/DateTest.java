package kellonge.flightcrawler.test;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import javassist.expr.NewArray;
import kellonge.flightcrawler.utils.DateTimeUtils;

public class DateTest {
	public static void main(String[] args) {
		Time time1 = Time.valueOf("8:00:00");
		Time time2 = Time.valueOf("7:12:00"); 
		System.out.println(DateTimeUtils.SubstractTime(time1, time2));
	}
}
