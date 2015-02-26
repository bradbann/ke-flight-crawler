package kellonge.flightcrawler.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Request.RequestStatus;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;

public class SpiderStatusMonitor {
	Spider spider;
	AtomicLong SuccessCnt = new AtomicLong(0);
	AtomicLong Fail_DownloadCnt = new AtomicLong(0);
	AtomicLong Fail_BlockingCnt = new AtomicLong(0);
	AtomicLong Fail_MaxRetryCnt = new AtomicLong(0);
	AtomicLong Fail_BizErrorCnt = new AtomicLong(0);
	AtomicLong Fail_UnknownCnt = new AtomicLong(0);

	public SpiderStatusMonitor(Spider spider) {
		this.spider = spider; 
		spider.addSpiderListener(listener);
	}

	private SpiderListener listener = new SpiderListener() {

		@Override
		public void onSuccess(Request request) {
			RequestStatus status = (RequestStatus) request
					.getExtra(Request.STATUS_ENUM);
			calCnt(status);
			System.out.println(getSipderStatus());
		}

		@Override
		public void onError(Request request) {
			RequestStatus status = (RequestStatus) request
					.getExtra(Request.STATUS_ENUM);
			calCnt(status);
			System.out.println(getSipderStatus());

		}

		@Override
		public void onFinish(Spider spider) {
			System.out.println("all request has finish.");
		}
	};

	private void calCnt(RequestStatus status) {
		if (status == RequestStatus.Fail_BizError) {
			Fail_BizErrorCnt.getAndIncrement();
		}
		if (status == RequestStatus.Fail_Blocking) {
			Fail_BlockingCnt.getAndIncrement();
		}
		if (status == RequestStatus.Fail_Download) {
			Fail_DownloadCnt.getAndIncrement();
		}
		if (status == RequestStatus.Fail_MaxRetry) {
			Fail_MaxRetryCnt.getAndIncrement();
		}
		if (status == RequestStatus.Fail_Unknown) {
			Fail_UnknownCnt.getAndIncrement();
		}
		if (status == RequestStatus.Success) {
			SuccessCnt.getAndIncrement();
		}
	}

	private String getSipderStatus() {
		String str = "";
		MonitorableScheduler monitorableScheduler = (MonitorableScheduler) spider
				.getScheduler();
		str += "totalQueue:"
				+ monitorableScheduler.getTotalRequestsCount(spider);
		str += " leftQueue:"
				+ monitorableScheduler.getLeftRequestsCount(spider);
		str += " totalRequest:" + spider.getPageCount();
		str += "\n";
		str += " Fail_BizErrorCnt:" + Fail_BizErrorCnt.get();
		str += " Fail_BlockingCnt:" + Fail_BlockingCnt.get();
		str += " Fail_DownloadCnt:" + Fail_DownloadCnt.get();
		str += " Fail_MaxRetryCnt:" + Fail_MaxRetryCnt.get();
		str += " Fail_UnknownCnt:" + Fail_UnknownCnt.get();
		str += " SuccessCnt:" + SuccessCnt.get();

		return str;
	}
}
