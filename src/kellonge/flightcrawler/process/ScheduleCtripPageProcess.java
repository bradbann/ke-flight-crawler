package kellonge.flightcrawler.process;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class ScheduleCtripPageProcess implements PageProcessor {
	private Site site = Site.me().setCycleRetryTimes(3).setRetryTimes(2)
			.setSleepTime(2000).setTimeOut(10000);

	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub

	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return null;
	}

}
