package kellonge.flightcrawler.extension;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpHost;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.thread.CountableThreadPool;

public class SpiderExtension extends Spider {

	private ReentrantLock newUrlLock = new ReentrantLock();

	private Condition newUrlCondition = newUrlLock.newCondition();

	private final AtomicLong pageCount = new AtomicLong(0);

	private int spiderID;

	private int emptySleepTime = 30000;

	public static SpiderExtension create(PageProcessor pageProcessor) {
		return new SpiderExtension(pageProcessor);
	}

	public SpiderExtension(PageProcessor pageProcessor) {
		super(pageProcessor);
		spiderID = new Random().nextInt();
	}

	protected void onFinish() {
		if (CollectionUtils.isNotEmpty(getSpiderListeners())) {
			for (SpiderListener spiderListener : getSpiderListeners()) {
				SpiderListenerExtension spiderListenerExtension = (SpiderListenerExtension) spiderListener;
				spiderListenerExtension.onFinish(this);
			}
		}
	}

	protected void checkRunningStat() {

		while (true) {
			int statNow = stat.get();
			if (statNow == STAT_RUNNING) {
				throw new IllegalStateException("Spider is already running!");
			}
			if (stat.compareAndSet(statNow, STAT_RUNNING)) {
				break;
			}
		}
	}

	protected void signalNewUrl() {
		try {
			newUrlLock.lock();
			newUrlCondition.signalAll();
		} finally {
			newUrlLock.unlock();
		}
	}

	protected void waitNewUrl() {
		newUrlLock.lock();
		try {
			// double check
			if (threadPool.getThreadAlive() == 0 && exitWhenComplete) {
				return;
			}
			newUrlCondition.await(emptySleepTime, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.warn("waitNewUrl - interrupted, error {}", e);
		} finally {
			newUrlLock.unlock();
		}
	}

	@Override
	public void run() {
		checkRunningStat();
		initComponent();
		logger.info("Spider " + getUUID() + " started!");
		while (!Thread.currentThread().isInterrupted()
				&& stat.get() == STAT_RUNNING) {
			Request request = scheduler.poll(this);
			if (request == null) {
				if (threadPool.getThreadAlive() == 0 && exitWhenComplete) {
					break;
				}
				// wait until new url added
				waitNewUrl();
			} else {
				final Request requestFinal = request;
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							processRequest(requestFinal);
							onSuccess(requestFinal);
						} catch (Exception e) {
							onError(requestFinal);
							logger.error("process request " + requestFinal
									+ " error", e);
						} finally {
							if (site.getHttpProxyPool() != null
									&& site.getHttpProxyPool().isEnable()) {
								site.returnHttpProxyToPool(
										(HttpHost) requestFinal
												.getExtra(Request.PROXY),
										(Integer) requestFinal
												.getExtra(Request.STATUS_CODE));
							}
							pageCount.incrementAndGet();
							signalNewUrl();
						}
					}
				});
			}
		}
		stat.set(STAT_STOPPED);
		// release some resources
		if (destroyWhenExit) {
			close();
		}
		onFinish();
	}

	@Override
	protected void processRequest(Request request) {
		Page rawPage = downloader.download(request, this);

		if (rawPage == null) {
			sleep(site.getSleepTime());
			onError(request);
			return;
		}
		PageExtension page = PageExtension.create(rawPage);
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

	public int getSpiderID() {
		return spiderID;
	}

}
