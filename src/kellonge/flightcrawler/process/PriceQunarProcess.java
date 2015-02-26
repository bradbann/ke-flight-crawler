package kellonge.flightcrawler.process;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import kellonge.flightcrawler.config.Configuration;
import kellonge.flightcrawler.model.FlightInfo; 
import kellonge.flightcrawler.utils.DateTimeUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Request.RequestStatus;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyPool;

public class PriceQunarProcess implements PageProcessor {

	public PriceQunarProcess() {
		List<String[]> httpProxyList = new ArrayList<String[]>();
		httpProxyList.addAll(Configuration.getProxys());
		if (Configuration.isUseProxy()) {
			ProxyPool proxyPool = site.enableHttpProxyPool().getHttpProxyPool();
			proxyPool.validateWhenInit(true);
			proxyPool.setWebGet(true);
			proxyPool
					.setMinProxy((int) Math.round(Configuration.getThreadNum() * 1.2));
			proxyPool.addProxy(httpProxyList.toArray(new String[httpProxyList
					.size()][]));
			proxyPool.checkAndGetProxyFromWeb();
			proxyPool.setProxyFilePath(Configuration.ROOT_PATH
					+ "/data/lastUse.proxy");
		}

	}

	private Site site = Site.me()
			.setCycleRetryTimes(Configuration.getCycleRetryTimes())
			.setSleepTime(Configuration.getSleepTime())
			.setTimeOut(Configuration.getTimeOut());

	@Override
	public void process(Page page) {
		String errorMsg = "";
		try {
			String json = page.getRawText();
			int findex = json.indexOf('(');
			int lindex = json.lastIndexOf(')');
			json = json.substring(findex + 1, lindex ); 
			JSONObject jsonObject = JSONObject.parseObject(json);
			JSONObject flightJosn = jsonObject
					.getJSONObject("oneway_data");
 

			if (flightJosn != null
					&& flightJosn.containsKey("recommendInfo")) {
				page.getRequest().getExtras()
						.put(Request.STATUS_ENUM, RequestStatus.Success);

			} else {
				page.setNeedCycleRetry(true);
				page.getRequest().getExtras()
						.put(Request.STATUS_ENUM, RequestStatus.Fail_BizError);
				page.getRequest().putExtra(Request.STATUS_MSG, errorMsg);
			}
		} catch (Exception e) {
			page.setNeedCycleRetry(true);
			page.getRequest().putExtra(Request.STATUS_ENUM,
					RequestStatus.Fail_Unknown);
			page.getRequest().putExtra(Request.STATUS_MSG, errorMsg);
		}

	}

	@Override
	public Site getSite() {
		return site;
	}

}
