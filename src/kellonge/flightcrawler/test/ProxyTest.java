package kellonge.flightcrawler.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;

import us.codecraft.webmagic.proxy.ProxyValidate;

public class ProxyTest {
	public static void main(String[] args) {
		List<HttpHost> hosts = new ArrayList<HttpHost>();
		try {
			hosts.add(new HttpHost(InetAddress.getByName("58.42.202.8"), 9797));

			hosts.add(new HttpHost(InetAddress.getByName("113.5.168.114"), 9797));

			hosts.add(new HttpHost(InetAddress.getByName("218.19.49.141"), 8090));
			hosts.add(new HttpHost(InetAddress.getByName("117.163.174.237"),
					8123));
			hosts.add(new HttpHost(InetAddress.getByName("120.206.77.174"),
					8123));
			hosts.add(new HttpHost(InetAddress.getByName("183.203.22.91"), 80));
			hosts.add(new HttpHost(InetAddress.getByName("117.171.165.152"),
					8123));
			hosts.add(new HttpHost(InetAddress.getByName("117.68.170.45"), 8118));
			hosts.add(new HttpHost(InetAddress.getByName("223.246.39.108"),
					9797));
			hosts.add(new HttpHost(InetAddress.getByName("222.87.129.218"), 843));
			hosts.add(new HttpHost(InetAddress.getByName("221.212.252.230"),
					1920));
			hosts.add(new HttpHost(InetAddress.getByName("183.223.200.149"),
					8123));
			hosts.add(new HttpHost(InetAddress.getByName("180.153.100.242"), 85));
			hosts.add(new HttpHost(InetAddress.getByName("114.221.44.101"),
					8088));
			hosts.add(new HttpHost(InetAddress.getByName("117.10.241.111"),
					8118));
			hosts.add(new HttpHost(InetAddress.getByName("124.88.67.13"), 80));
			hosts.add(new HttpHost(InetAddress.getByName("114.47.6.214"), 9064));
			hosts.add(new HttpHost(InetAddress.getByName("117.163.158.56"),
					8123));
			hosts.add(new HttpHost(InetAddress.getByName("124.119.77.73"), 8088));
			hosts.add(new HttpHost(InetAddress.getByName("121.61.8.168"), 9797));
			hosts.add(new HttpHost(InetAddress.getByName("120.198.243.115"),
					8081));
			hosts.add(new HttpHost(InetAddress.getByName("111.195.73.6"), 8118));
			hosts.add(new HttpHost(InetAddress.getByName("42.122.142.233"),
					8090));
			hosts.add(new HttpHost(InetAddress.getByName("123.132.200.118"),
					9797));
			hosts.add(new HttpHost(InetAddress.getByName("115.203.65.166"),
					8585));
			hosts.add(new HttpHost(InetAddress.getByName("111.176.138.178"),
					18186));
			hosts.add(new HttpHost(InetAddress.getByName("59.50.165.253"), 8118));
			hosts.add(new HttpHost(InetAddress.getByName("119.49.191.235"),
					8118));
			hosts.add(new HttpHost(InetAddress.getByName("114.228.176.225"),
					8118));
			hosts.add(new HttpHost(InetAddress.getByName("210.73.1.144"), 8118));
			hosts.add(new HttpHost(InetAddress.getByName("60.218.30.39"), 9797));
			hosts.add(new HttpHost(InetAddress.getByName("117.165.17.96"), 8123));
			hosts.add(new HttpHost(InetAddress.getByName("120.35.83.193"), 8118));
			hosts.add(new HttpHost(InetAddress.getByName("122.246.144.37"),
					8090));
			hosts.add(new HttpHost(InetAddress.getByName("116.209.8.251"), 8118));
			hosts.add(new HttpHost(InetAddress.getByName("222.160.34.102"),
					18186));
			hosts.add(new HttpHost(InetAddress.getByName("218.93.104.210"),
					8118));
			hosts.add(new HttpHost(InetAddress.getByName("110.18.232.225"),
					8118));
			hosts.add(new HttpHost(InetAddress.getByName("39.188.174.87"), 8123));
			hosts.add(new HttpHost(InetAddress.getByName("121.14.138.56"), 81));
			hosts.add(new HttpHost(InetAddress.getByName("117.171.242.73"),
					8123));
			hosts.add(new HttpHost(InetAddress.getByName("113.25.65.247"), 8118));
			hosts.add(new HttpHost(InetAddress.getByName("218.22.254.197"),
					63000));
			hosts.add(new HttpHost(InetAddress.getByName("223.83.143.43"), 8123));
			hosts.add(new HttpHost(InetAddress.getByName("183.203.22.103"), 80));
			hosts.add(new HttpHost(InetAddress.getByName("24.224.128.222"),
					9064));
			hosts.add(new HttpHost(InetAddress.getByName("103.254.208.53"),
					3128));
			hosts.add(new HttpHost(InetAddress.getByName("127.73.236.18"), 80));
			hosts.add(new HttpHost(InetAddress.getByName("185.68.154.178"),
					8080));
			hosts.add(new HttpHost(InetAddress.getByName("191.189.148.141"),
					3128));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ProxyValidate.Validate(hosts));
		System.out.println("ssw");
	}

}