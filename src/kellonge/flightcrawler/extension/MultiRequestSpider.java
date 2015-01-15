package kellonge.flightcrawler.extension;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class MultiRequestSpider extends Spider {

	public static MultiRequestSpider create(PageProcessor pageProcessor) {
		return new MultiRequestSpider(pageProcessor);
	}

	public MultiRequestSpider(PageProcessor pageProcessor) {
		super(pageProcessor);
	}

	@Override
	protected void processRequest(Request request) {
		Page rawPage = downloader.download(request, this);

		if (rawPage == null) {
			sleep(site.getSleepTime());
			onError(request);
			return;
		}
		MultiRequestPage page = MultiRequestPage.create(rawPage);
		// for cycle retry
		if (page.isNeedCycleRetry()) {
			extractAndAddRequests(page, true);
			sleep(site.getSleepTime());
			return;
		}
		pageProcessor.process(page);
		extractAndAddRequests(page, spawnUrl);
		if (!page.getResultItems().isSkip()) {
			for (Pipeline pipeline : pipelines) {
				pipeline.process(page.getResultItems(), this);
			}
		}
		// page need new request
		if (page.getContinueRequest() != null) {
			processRequest(page.getContinueRequest());
		}
		// for proxy status management
		request.putExtra(Request.STATUS_CODE, page.getStatusCode());
		sleep(site.getSleepTime());
	}

}
