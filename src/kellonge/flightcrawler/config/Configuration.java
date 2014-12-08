package kellonge.flightcrawler.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
	private static List<String> UserAgents = new ArrayList<String>();
	private static List<String[]> Proxys = new ArrayList<String[]>();
	private static int ThreadNum = 1;
	private static int CycleRetryTimes = 1;
	private static int SleepTime = 6000;
	private static int TimeOut = 5000;

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

}
