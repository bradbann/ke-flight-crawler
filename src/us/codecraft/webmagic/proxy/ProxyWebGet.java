package us.codecraft.webmagic.proxy;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import kellonge.flightcrawler.config.Configuration;
import kellonge.flightcrawler.utils.SimpleHttpRequest;
import kellonge.flightcrawler.utils.Utility;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.jsoup.helper.StringUtil;

public class ProxyWebGet {

	public static List<String[]> getNewProxy(String url) {
		System.out.println(url);
		if (StringUtils.isEmpty(url)) {
			url = urlBuilder(10);
		}
		List<String[]> hosts = new ArrayList<String[]>();
		String data = SimpleHttpRequest.Get(url);
		String[] dataArr = data.split("\\n");
		for (String string : dataArr) {
			String[] ipAndPortArr = string.split(":");
			if (ipAndPortArr.length == 2) {
				ipAndPortArr[0] = ipAndPortArr[0].trim();
				ipAndPortArr[1] = ipAndPortArr[1].trim();
				hosts.add(ipAndPortArr);
			}
		}
		return hosts;
	}

	/**
	 * 构造请求参数
	 * 
	 * @param num
	 *            每次取的数量
	 * @return
	 */
	public static String urlBuilder(int num) {
		String rtv = Configuration.getProxyGetAddress();
		rtv.trim();
		rtv += "&num=" + num;
		rtv += "&filter=on";
		return rtv;
	}
}
