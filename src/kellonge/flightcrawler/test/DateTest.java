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
import kellonge.flightcrawler.utils.Utility;

public class DateTest {
	public static void main(String[] args) {
		System.out.println(DateTimeUtils.getWeekOfDate(new Date()));
		System.out.println(DateTimeUtils.getWeekOfDate(Utility
				.toSafeDateTime("2015-1-19")));
		System.out.println(DateTimeUtils.getWeekOfDate(Utility
				.toSafeDateTime("2015-1-25")));
	}
}
