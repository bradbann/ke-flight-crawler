package kellonge.flightcrawler.process;

import java.util.regex.*;

import kellonge.flightcrawler.config.Configuration;
import kellonge.flightcrawler.extension.MultiRequestPage;
import kellonge.flightcrawler.extension.MultiRequestSpider;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.xsoup.ElementOperator.Regex;

public class PriceCtripProcess implements PageProcessor {

	private Site site = Site.me()
			.setCycleRetryTimes(Configuration.getCycleRetryTimes())
			.setSleepTime(Configuration.getSleepTime())
			.setTimeOut(Configuration.getTimeOut());

	@Override
	public void process(Page page) {		
		if (page.getUrl().regex("flights.ctrip.com/booking").match()) {
			MultiRequestPage multiRequestPage = (MultiRequestPage) page;
			String rand = page.getHtml().regex("ajaxRequest\\(url.*\'\\);")
					.regex("\\d\\.\\d+").get();
			String url = page
					.getHtml()
					.regex("http://flights.ctrip.com/domesticsearch/search/SearchFirstRouteFlights.*CK=[0-9A-z]+")
					.get();
			url += "&r=" + rand;
			Request request = new Request(url);
			site.addHeader("Referer", page.getUrl().toString());
			multiRequestPage.setContinueRequest(request);
		} else {
			
			
			System.out.println(page.getJson());
		}

	}

	@Override
	public Site getSite() {
		return site;
	}

}
