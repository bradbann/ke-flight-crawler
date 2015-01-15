package kellonge.flightcrawler.extension;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

public class MultiRequestPage extends Page {

	public static MultiRequestPage create(Page page) {
		MultiRequestPage multiRequestPage = new MultiRequestPage();
		multiRequestPage.setHtml(page.getHtml());
		multiRequestPage.setNeedCycleRetry(page.isNeedCycleRetry());
		multiRequestPage.setRawText(page.getRawText());
		multiRequestPage.setRequest(page.getRequest());
		multiRequestPage.setSkip(page.getResultItems().isSkip());
		multiRequestPage.setStatusCode(page.getStatusCode());
		multiRequestPage.setUrl(page.getUrl());
		return multiRequestPage;
	}

	public MultiRequestPage() {
	}

	private Request continueRequest;

	public Request getContinueRequest() {
		return continueRequest;
	}

	public void setContinueRequest(Request continueRequest) {
		this.continueRequest = continueRequest;
	}
}
