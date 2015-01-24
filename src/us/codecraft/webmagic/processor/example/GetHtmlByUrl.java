package us.codecraft.webmagic.processor.example;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.SimplePageProcessor;

public class GetHtmlByUrl implements PageProcessor {
	public static void main(String[] args) {
		Spider.create(new GetHtmlByUrl())
				.addUrl("http://webflight.linkosky.com/WEB/Flight/WaitingSearch.aspx?JT=1&OC=PEK&DC=SHA&dstDesp=GUANGZHOU%B9%E3%D6%DD&dst2=CAN&DD=2014-11-20&DT=7&BD=&BT=7&AL=ALL&DR=true&image.x=38&image.y=15")
				.start();

	}

	@Override
	public void process(Page page) {
		page.putField("rawHtml", page.getHtml());
		String reg = "/WEB/Flight/FlightSearchResultDefault.aspx.*(?='\\))";
		page.addTargetRequest(page.getHtml().regex(reg, 0).get());

	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
			.setTimeOut(10000)
			.addHeader("Referer", "http://www.caac.gov.cn/S1/GNCX/");
}
