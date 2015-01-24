package kellonge.flightcrawler.test;

import java.io.Console;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.JMException;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.google.common.util.concurrent.Monitor;

import kellonge.flightcrawler.model.FlightSchedule;
import kellonge.flightcrawler.model.FlightPrice;
import kellonge.flightcrawler.utils.DateTimeUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class MainTest1 implements PageProcessor {

	public static SessionFactory sessionFactory = new Configuration()
			.configure().buildSessionFactory();

	private static final String OUTPUTPATH = "/home/kellonge/Test";
	private Site site = Site.me().setCycleRetryTimes(3).setRetryTimes(3)
			.setSleepTime(1000).setTimeOut(10000)
			.addHeader("Referer", "http://www.caac.gov.cn/S1/GNCX/");

	@Override
	public void process(Page page) {
		if (page.getUrl().regex("/WEB/Flight/FlightSearchResultDefault.aspx")
				.match()) {
			page.putField(
					"FlightInfo",
					page.getHtml().xpath(
							"//div[@class='layout2_title3']/text()"));

			page.putField("ExpiredDate",
					page.getHtml().xpath("//div[@class='layout2_title3']")
							.regex("\\d{4}-\\d{1,2}-\\d{1,2}"));
			List<Selectable> flightInfoNode = page
					.getHtml()
					.xpath("//div[@id='FlightListFlight0']/div[@class='menu_layout2']")
					.nodes();
			List<Selectable> priceInfoNode = page
					.getHtml()
					.xpath("//div[@id='FlightListFlight0']/div[@class='menu_content_small1']")
					.nodes();
			if (!flightInfoNode.isEmpty() && !priceInfoNode.isEmpty()
					&& flightInfoNode.size() == priceInfoNode.size()) {
				Map<FlightSchedule, List<FlightPrice>> datas = new HashMap<>();
				for (int i = 0; i < flightInfoNode.size(); i++) {
					FlightSchedule flight = new FlightSchedule();
					flight.setFlag(1);
					flight.setExpiredDate(DateTimeUtils.parse(page.getResultItems()
							.get("ExpiredDate").toString(),
							DateTimeUtils.FORMAT_SHORT));
					flight.setAirLineName(flightInfoNode.get(i)
							.xpath("//div[@class='menu_top1']/text()")
							.toString().trim());
					flight.setFlightNo(flightInfoNode.get(i)
							.xpath("//span[@class='red_font']/text()")
							.toString().trim());
					flight.setPlanModel(flightInfoNode.get(i)
							.xpath("//div[@class='menu_top2']/text()").nodes()
							.get(1).regex("[a-zA-Z\\d]+").get());

					flight.setDeptAirportName(priceInfoNode
							.get(i)
							.xpath("//div[@class='listone_layout']/div[@class='menu1_layout']/text()")
							.toString().trim());
					flight.setArrAirportName(priceInfoNode
							.get(i)
							.xpath("//div[@class='listone_layout']/div[@class='menu1_layout']/text()")
							.all().get(1).toString().trim());

					flight.setDeptTime(DateTimeUtils
							.parseTime(priceInfoNode
									.get(i)
									.xpath("//div[@class='listone_layout']/div[@class='menu2_layout']/text()")
									.regex("\\d{2}:\\d{2}").get()
									+ ":00"));
					flight.setArrTime(DateTimeUtils
							.parseTime(priceInfoNode
									.get(i)
									.xpath("//div[@class='listone_layout']/div[@class='menu2_layout']/text()")
									.nodes().get(1).regex("\\d{2}:\\d{2}")
									.get()
									+ ":00"));

					datas.put(flight, null);

				}
				page.putField("FlightDatas", datas);
			} else {
				page.setSkip(true);
			}
		} else {
			page.addTargetRequest(page
					.getHtml()
					.regex("/WEB/Flight/FlightSearchResultDefault.aspx.*(?='\\))",
							0).get());
			page.setSkip(true);
		}
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		List<String> urls = new ArrayList<String>();
		for (int i = 0; i < citys.length; i++) {
			for (int j = 0; j < citys.length; j++) {
				if (i == j) {
					break;
				}
				urls.add(String
						.format("http://webflight.linkosky.com/WEB/Flight/WaitingSearch.aspx?JT=1&OC=%s&DC=%s&DD=2014-11-20&DT=7&BD=&BT=7&AL=ALL&DR=true",
								citys[i], citys[j]));
			}
		}

		Spider flightCrawler = Spider.create(new MainTest1()).thread(10)
				.addUrl(urls.toArray(new String[urls.size()]))
				.addPipeline(new ToDatebasePipline())
				.addPipeline(new ConsolePipeline());

		flightCrawler.start();

	}

	private static final String[] citys = { "AKU", "AAT", "AKA", "AQG", "AOG",
			"BSD", "BAV", "BHY", "PEK", "NAY", "BFU", "CGQ", "CGD", "CSX",
			"CIH", "CZX", "CHG", "CTU", "CIF", "CKG", "DAX", "DLC", "DLU",
			"DDG", "DAT", "DOY", "DNH", "ENH", "FUG", "FYN", "FOC", "KOW",
			"GOQ", "GHN", "CAN", "KWL", "KWE", "HRB", "HAK", "HLD", "HMI",
			"HGH", "HZG", "HFE", "HEK", "HKG", "HNY", "HTN", "TXN", "HYN",
			"HET", "KNC", "JMU", "JGN", "JIL", "TNA", "JNG", "JDZ", "JHG",
			"JJN", "JNZ", "CHW", "JIU", "JZH", "KRY", "KHG", "KRL", "KMG",
			"KCA", "LHW", "LXA", "LYG", "LJG", "LYI", "LZH", "LYA", "LZO",
			"MFM", "LUM", "NZH", "MXZ", "MIG", "MDG", "KHN", "NAO", "NKG",
			"NNG", "NTG", "NNY", "NGB", "IQM", "TAO", "IQN", "SHP", "NDG",
			"JJN", "JUZ", "SYX", "SHA", "PVG", "SWA", "SHS", "SZX", "SHE",
			"SJW", "SYM", "SZV", "TCG", "TYN", "TSN", "TNH", "TGO", "TEN",
			"URC", "WXN", "WEF", "WEH", "WNZ", "WUH", "WJD", "HLH", "WUS",
			"WUX", "WUZ", "XMN", "XIY", "XFN", "XIC", "XIL", "XNN", "XUZ",
			"ENY", "YNJ", "YNT", "YNZ", "YBP", "YIH", "INC", "YIN", "YIW",
			"LLF", "UYN", "ZAT", "DYG", "ZHA", "HJJ", "DIG", "CGO", "HSN",
			"ZUH", "ZYI" };
}
