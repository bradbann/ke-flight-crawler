package us.codecraft.webmagic.downloader;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Request.RequestStatus;
import us.codecraft.webmagic.selector.Html;

/**
 * Base class of downloader with some common methods.
 *
 * @author code4crafter@gmail.com
 * @since 0.5.0
 */
public abstract class AbstractDownloader implements Downloader {

	/**
	 * A simple method to download a url.
	 *
	 * @param url
	 * @return html
	 */
	public Html download(String url) {
		return download(url, null);
	}

	/**
	 * A simple method to download a url.
	 *
	 * @param url
	 * @return html
	 */
	public Html download(String url, String charset) {
		Page page = download(new Request(url), Site.me().setCharset(charset)
				.toTask());
		return (Html) page.getHtml();
	}

	protected void onSuccess(Request request) {
	}

	protected void onError(Request request) {
		request.getExtras().put(Request.STATUS_ENUM,
				RequestStatus.Fail_Download);
	}

	public Page addToCycleRetry(Request request, Site site) {
		Page page = new Page();
		Object cycleTriedTimesObject = request
				.getExtra(Request.CYCLE_TRIED_TIMES);
		if (cycleTriedTimesObject == null) {
			page.addTargetRequest(request.setPriority(0)
					.putExtra(Request.CYCLE_TRIED_TIMES, 1)
					.putExtra(Request.STATUS_ENUM, RequestStatus.Init));
		} else {
			int cycleTriedTimes = (Integer) cycleTriedTimesObject;

			cycleTriedTimes++;

			if (cycleTriedTimes >= site.getCycleRetryTimes()) {
				request.getExtras().put(Request.STATUS_ENUM,
						RequestStatus.Fail_MaxRetry);
				return null;
			}
			page.addTargetRequest(request.setPriority(0)
					.putExtra(Request.CYCLE_TRIED_TIMES, cycleTriedTimes)
					.putExtra(Request.STATUS_ENUM, RequestStatus.Init));
		}
		page.setNeedCycleRetry(true);
		request.getExtras().put(Request.STATUS_ENUM,
				RequestStatus.Fail_Download);
		return page;
	}
}
