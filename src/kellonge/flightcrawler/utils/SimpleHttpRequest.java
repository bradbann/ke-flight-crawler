package kellonge.flightcrawler.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class SimpleHttpRequest {

	public static String Get(String requestUrl) {
		URL url;
		HttpURLConnection httpURLConn = null;
		StringBuilder sb = new StringBuilder();
		try {
			String temp = new String();
			url = new URL(requestUrl);
			httpURLConn = (HttpURLConnection) url.openConnection();
			httpURLConn.setDoOutput(true);
			httpURLConn.setRequestMethod("GET");
			httpURLConn.setIfModifiedSince(999999999);
			httpURLConn.connect();
			InputStream in = httpURLConn.getInputStream();
			sb.append(IOUtils.toString(in));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpURLConn != null) {
				httpURLConn.disconnect();
			}
		}
		return sb.toString();
	}
}
