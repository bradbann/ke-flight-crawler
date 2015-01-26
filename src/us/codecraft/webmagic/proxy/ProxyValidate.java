package us.codecraft.webmagic.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyValidate {

	private static Logger logger = LoggerFactory.getLogger(ProxyValidate.class);

	private static ExecutorService threadPool;

	private static ExecutorService getThreadPool() {
		if (threadPool == null) {
			threadPool = Executors.newFixedThreadPool(10);

		}
		return threadPool;
	}

	public static List<HttpHost> Validate(List<HttpHost> hosts) {
		List<HttpHost> rtv = new ArrayList<HttpHost>();
		List<FutureTask<HttpHost>> tasks = new ArrayList<FutureTask<HttpHost>>();
		for (HttpHost httpHost : hosts) {
			final HttpHost finalHttpHost = httpHost;
			FutureTask<HttpHost> task = new FutureTask<HttpHost>(
					new Callable<HttpHost>() {

						@Override
						public HttpHost call() throws Exception {
							if (isValid(finalHttpHost.getAddress()
									.getHostAddress(), finalHttpHost.getPort())) {
								return finalHttpHost;
							}
							return null;
						}
					});
			tasks.add(task);
			getThreadPool().submit(task);
		}
		for (FutureTask<HttpHost> futureTask : tasks) {
			HttpHost httpHost;
			try {
				httpHost = futureTask.get();
				if (httpHost != null) {
					rtv.add(httpHost);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

		}
		return rtv;
	}

	public static boolean isValid(String ip, int port) {
		boolean rtv = false;
		URL url = null;
		try {
			url = new URL("http://www.baidu.com");
		} catch (MalformedURLException e) {
			System.out.println("url invalidate");
		}
		InetSocketAddress addr = null;
		addr = new InetSocketAddress(ip, port);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); // http proxy
		try {
			URLConnection conn = url.openConnection(proxy);
			conn.setConnectTimeout(1000);
			Map<String, List<String>> heads = conn.getHeaderFields();
			if (heads != null) {
				List<String> head = heads.get(null);
				if (head.size() > 0 && head.get(0).indexOf("200") >= 0) {
					rtv = true;
				}

			}
		} catch (Exception e) {
			logger.info("ip " + ip + " is not aviable");
		}
		if (rtv) {
			logger.info("ip " + ip + " is ok");
		}
		return rtv;
	}
}
