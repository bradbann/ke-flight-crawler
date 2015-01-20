package kellonge.flightcrawler.extension;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;

public interface SpiderListenerExtension extends SpiderListener {
	public void onFinish(Spider spider);
}
