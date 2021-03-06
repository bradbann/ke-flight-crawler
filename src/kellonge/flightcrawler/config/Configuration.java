package kellonge.flightcrawler.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kellonge.flightcrawler.utils.Utility;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * @author kellonge
 *
 */
public class Configuration {
	public static final String ROOT_PATH = System.getProperty("user.dir");
	public static final String CONFIG_PATH = ROOT_PATH + "/crawler.xml";
	private static final long CONFIG_REFRESH = 60000L;
	private static Logger logger = Logger.getLogger(Configuration.class);

	/* config value */
	private static String DataPath = ROOT_PATH + "/data";
	private static String HibernateConfig = ROOT_PATH + "/hibernate.cfg.xml";
	private static List<String> UserAgents = new ArrayList<String>();
	private static List<String[]> Proxys = new ArrayList<String[]>();
	private static int ThreadNum = 1;
	private static int CycleRetryTimes = 1;
	private static int SleepTime = 6000;
	private static int TimeOut = 5000;
	private static int ProxyPoolItemRetry = 5;
	private static boolean UseCachedQueue = false;
	private static boolean UseProxy = false;
	private static boolean AutoRestart = false;
	private static String SpiderName = "";
	private static String ProxyGetAddress = "";
	private static int AHeadDay = 30;

	/**
	 * 初始化配置
	 */
	public static void init() {
		readConfig();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				readConfig();
			}
		};
		Timer timer = new Timer();
		timer.schedule(timerTask, CONFIG_REFRESH, CONFIG_REFRESH);

	}

	public static String getDataPath() {
		return DataPath;
	}

	public static List<String> getUserAgents() {
		return UserAgents;
	}

	public static List<String[]> getProxys() {
		return Proxys;
	}

	private static void readConfig() {
		logger.info("load config");
		if (!Files.exists(Paths.get(CONFIG_PATH))) {
			logger.warn("config file not exist in path:" + CONFIG_PATH);
			return;
		}

		SAXReader reader = new SAXReader();
		Document doc;
		try {
			doc = reader.read(CONFIG_PATH);
			/* proxy */
			List<Node> proxyList = doc.selectNodes("//proxy/item");
			if (proxyList != null) {
				Proxys.clear();
				for (Node proxy : proxyList) {
					Proxys.add(new String[] { proxy.valueOf("@ip"),
							proxy.valueOf("@port") });
				}
			}
			/* user-agent */
			List<Node> userAgentList = doc.selectNodes("//user-agent/item");
			if (userAgentList != null) {
				UserAgents.clear();
				for (Node userAgent : userAgentList) {
					UserAgents.add(userAgent.valueOf("@name"));
				}
			}
			/* data-root */
			Node dataPathNode = doc
					.selectSingleNode("//crawler-common/data-path");
			if (dataPathNode != null && !dataPathNode.getText().equals("")) {

				DataPath = dataPathNode.getText();
			} else {
				DataPath = ROOT_PATH + "/data";
			}
			if (!Files.exists(Paths.get(DataPath))) {
				Files.createDirectories(Paths.get(DataPath));
			}
			/* other value */
			Node threadNum = doc
					.selectSingleNode("//crawler-common/thread-num");
			if (threadNum != null && !threadNum.getText().equals("")) {
				setThreadNum(Integer.valueOf(threadNum.getText()));
			}

			Node cycleRetryNum = doc
					.selectSingleNode("//crawler-common/cycle-retry-times");
			if (cycleRetryNum != null && !cycleRetryNum.getText().equals("")) {
				setCycleRetryTimes(Integer.valueOf(cycleRetryNum.getText()));
			}

			Node sleepTime = doc
					.selectSingleNode("//crawler-common/sleep-time");
			if (sleepTime != null && !sleepTime.getText().equals("")) {
				setSleepTime(Integer.valueOf(sleepTime.getText()));
			}

			Node timeOut = doc.selectSingleNode("//crawler-common/time-out");
			if (timeOut != null && !timeOut.getText().equals("")) {
				setTimeOut(Integer.valueOf(timeOut.getText()));
			}

			Node useCachedQueue = doc
					.selectSingleNode("//crawler-common/use-cached-queue");
			if (useCachedQueue != null && !useCachedQueue.getText().equals("")) {
				setUseCachedQueue(Integer.valueOf(useCachedQueue.getText()) == 1);
			}

			Node useProxy = doc.selectSingleNode("//crawler-common/use-proxy");
			if (useProxy != null && !useProxy.getText().equals("")) {
				setUseProxy(Integer.valueOf(useProxy.getText()) == 1);
			}
			Node autoRestart = doc
					.selectSingleNode("//crawler-common/auto-restart");
			if (autoRestart != null && !autoRestart.getText().equals("")) {
				setAutoRestart(Integer.valueOf(autoRestart.getText()) == 1);
			}
			Node spiderName = doc
					.selectSingleNode("//crawler-common/spider-name");
			if (spiderName != null && !spiderName.getText().equals("")) {
				setSpiderName(spiderName.getText());
			}

			Node proxyGetAddress = doc
					.selectSingleNode("//crawler-common/proxy-get-address");
			if (proxyGetAddress != null
					&& !proxyGetAddress.getText().equals("")) {
				setProxyGetAddress(proxyGetAddress.getText().trim());
			}

			Node proxyRetry = doc
					.selectSingleNode("//crawler-common/proxy-retry");
			if (proxyRetry != null && !proxyRetry.getText().equals("")) {
				setProxyPoolItemRetry(Utility.toSafeInt(proxyRetry.getText()
						.trim()));
			}

			Node aHeadDay = doc.selectSingleNode("//crawler-common/ahead-day");
			if (aHeadDay != null && !aHeadDay.getText().equals("")) {
				setAHeadDay(Utility.toSafeInt(aHeadDay.getText().trim()));
			}
		} catch (DocumentException | IOException e) {
			logger.warn("init config faild", e);
		} catch (Exception e) {
			logger.warn("init config faild", e);
		}
	}

	public static int getThreadNum() {
		return ThreadNum;
	}

	public static void setThreadNum(int threadNum) {
		ThreadNum = threadNum;
	}

	public static int getCycleRetryTimes() {
		return CycleRetryTimes;
	}

	public static void setCycleRetryTimes(int cycleRetryTimes) {
		CycleRetryTimes = cycleRetryTimes;
	}

	public static int getSleepTime() {
		return SleepTime;
	}

	public static void setSleepTime(int sleepTime) {
		SleepTime = sleepTime;
	}

	public static int getTimeOut() {
		return TimeOut;
	}

	public static void setTimeOut(int timeOut) {
		TimeOut = timeOut;
	}

	public static boolean isUseCachedQueue() {
		return UseCachedQueue;
	}

	public static void setUseCachedQueue(boolean useCachedQueue) {
		UseCachedQueue = useCachedQueue;
	}

	public static boolean isUseProxy() {
		return UseProxy;
	}

	public static void setUseProxy(boolean useProxy) {
		UseProxy = useProxy;
	}

	public static String getHibernateConfig() {
		return HibernateConfig;
	}

	public static void setHibernateConfig(String hibernateConfig) {
		HibernateConfig = hibernateConfig;
	}

	public static boolean isAutoRestart() {
		return AutoRestart;
	}

	public static void setAutoRestart(boolean autoRestart) {
		AutoRestart = autoRestart;
	}

	public static String getSpiderName() {
		return SpiderName;
	}

	public static void setSpiderName(String spiderName) {
		SpiderName = spiderName;
	}

	public static String getProxyGetAddress() {
		return ProxyGetAddress;
	}

	public static void setProxyGetAddress(String proxyGetAddress) {
		ProxyGetAddress = proxyGetAddress;
	}

	public static int getProxyPoolItemRetry() {
		return ProxyPoolItemRetry;
	}

	public static void setProxyPoolItemRetry(int proxyPoolItemRetry) {
		ProxyPoolItemRetry = proxyPoolItemRetry;
	}

	public static int getAHeadDay() {
		return AHeadDay;
	}

	public static void setAHeadDay(int aHeadDay) {
		AHeadDay = aHeadDay;
	}

}
