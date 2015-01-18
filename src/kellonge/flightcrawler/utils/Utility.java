package kellonge.flightcrawler.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Utility {

	public static String[] getStrArray(String str) {
		String strs[] = str.split(",");
		ArrayList arr = new ArrayList();
		String as[];
		int j = (as = strs).length;
		for (int i = 0; i < j; i++) {
			String s = as[i];
			arr.add(s);
		}

		Collections.sort(arr);
		return (String[]) arr.toArray(new String[arr.size()]);
	}

	public static String toSafeString(Object obj) {
		if (obj == null)
			return "";
		return obj.toString().trim();

	}

	public static int toSafeInt(Object obj) {
		if (isInteger(obj))
			return Integer.parseInt(toSafeString(obj));
		else
			return -1;
	}

	public static int toSafeInt(Object obj, int nInitValue) {
		int nResult = toSafeInt(obj);
		if (nResult == -1)
			nResult = nInitValue;
		return nResult;
	}

	public static long toSafeLong(Object obj) {
		long num = 0L;
		try {
			num = Long.parseLong(toSafeString(obj));
		} catch (Exception exception) {
		}
		return num;
	}

	public static long toSafeShort(Object obj) {
		short num = 0;
		try {
			num = Short.parseShort(toSafeString(obj));
		} catch (Exception exception) {
		}
		return (long) num;
	}

	public static float toSafeFloat(Object obj) {
		float num = 0.0F;
		try {
			num = Float.parseFloat(toSafeString(obj));
		} catch (Exception exception) {
		}
		return num;
	}

	public static double toSafeDouble(Object obj) {
		double num = 0.0D;
		try {
			num = Double.parseDouble(toSafeString(obj));
		} catch (Exception exception) {
		}
		return num;
	}

	public static Date toSafeDateTime(Object objDate) {
		Date date = new Date();
		try {
			String strDate = toSafeString(objDate);
			if (strDate.indexOf(":") > 0) {
				String strFormat = "yyyy-MM-dd HH:mm:ss";
				if (strDate.indexOf("/") > 0)
					strFormat = "yyyy/MM/dd HH:mm:ss";
				SimpleDateFormat formatDateTime = new SimpleDateFormat(
						strFormat);
				date = formatDateTime.parse(strDate);
			} else {
				String strFormat = "yyyy-MM-dd";
				if (strDate.indexOf("/") > 0)
					strFormat = "yyyy/MM/dd";
				SimpleDateFormat formatDate = new SimpleDateFormat(strFormat);
				date = formatDate.parse(strDate);
			}
		} catch (Exception exception) {
		}
		return date;
	}

	public static boolean toSafeBool(Object obj) {
		return toSafeString(obj).equalsIgnoreCase("true");
	}

	public static boolean isInteger(Object str) {
		try {
			Integer.parseInt(toSafeString(str));
		} catch (Exception err) {
			return false;
		}
		return true;
	}

}
