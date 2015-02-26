package us.codecraft.webmagic;

import us.codecraft.webmagic.utils.Experimental;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Object contains url to crawl.<br>
 * It contains some additional information.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public class Request implements Serializable {

	private static final long serialVersionUID = 2062192774891352043L;

	public static final String CYCLE_TRIED_TIMES = "_cycle_tried_times";
	public static final String STATUS_CODE = "statusCode";
	public static final String STATUS_ENUM = "statusEnum";
	public static final String STATUS_MSG = "statusMsg";
	public static final String PROXY = "proxy";

	private Map<String, String> requestHeads;

	private boolean isFinsih = true;

	private String initUrl;

	private String url;

	private String method;
	/**
	 * Store additional information in extras.
	 */
	private Map<String, Object> extras;

	/**
	 * Priority of the request.<br>
	 * The bigger will be processed earlier. <br>
	 * 
	 * @see us.codecraft.webmagic.scheduler.PriorityScheduler
	 */
	private long priority;

	public Request() {
		putExtra(STATUS_ENUM, RequestStatus.Init);
	}

	public Request(String url) {
		putExtra(STATUS_ENUM, RequestStatus.Init);
		this.url = url;
		this.initUrl = url;
	}

	public long getPriority() {
		return priority;
	}

	/**
	 * Set the priority of request for sorting.<br>
	 * Need a scheduler supporting priority.<br>
	 * 
	 * @see us.codecraft.webmagic.scheduler.PriorityScheduler
	 *
	 * @param priority
	 * @return this
	 */
	@Experimental
	public Request setPriority(long priority) {
		this.priority = priority;
		return this;
	}

	public Object getExtra(String key) {
		if (extras == null) {
			return null;
		}
		return extras.get(key);
	}

	public Request putExtra(String key, Object value) {
		if (extras == null) {
			extras = new HashMap<String, Object>();
		}
		extras.put(key, value);
		return this;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Request request = (Request) o;

		if (!url.equals(request.url))
			return false;

		return true;
	}

	public Map<String, Object> getExtras() {
		return extras;
	}

	@Override
	public int hashCode() {
		return url.hashCode();
	}

	public void setExtras(Map<String, Object> extras) {
		this.extras = extras;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * The http method of the request. Get for default.
	 * 
	 * @return httpMethod
	 * @see us.codecraft.webmagic.utils.HttpConstant.Method
	 * @since 0.5.0
	 */
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String toString() {
		return "Request{" + "url='" + url + '\'' + ", method='" + method + '\''
				+ ", extras=" + extras + ", priority=" + priority + '}';
	}

	public String getInitUrl() {
		return initUrl;
	}

	public void setInitUrl(String initUrl) {
		this.initUrl = initUrl;
	}

	public Map<String, String> getRequestHeads() {
		return requestHeads;
	}

	public void setRequestHeads(Map<String, String> requestHeads) {
		this.requestHeads = requestHeads;
	}

	public boolean isFinsih() {
		return isFinsih;
	}

	public void setFinsih(boolean isFinsih) {
		this.isFinsih = isFinsih;
	}

	public enum RequestStatus {
		Init, Processing, Success, Fail_Download, Fail_Blocking, Fail_MaxRetry, Fail_BizError, Fail_Unknown
	}

	// public boolean CheckRequestStatusCanContinue(Request request) {
	// RequestStatus status = (RequestStatus) request
	// .getExtra(Request.STATUS_ENUM);
	// if (status == RequestStatus.Fail_BizError
	// || status == RequestStatus.Fail_Unknown) {
	// return false;
	// } else {
	// return true;
	// }
	// }

}
