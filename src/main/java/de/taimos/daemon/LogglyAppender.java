package de.taimos.daemon;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import de.taimos.httputils.WS;

public class LogglyAppender extends AppenderSkeleton {
	
	private static final String BASE_URL = "https://logs-01.loggly.com/inputs/";
	
	private String url;
	
	private LinkedBlockingQueue<LoggingEvent> eventQueue = new LinkedBlockingQueue<>();
	
	private Executor executor = Executors.newSingleThreadExecutor();
	
	
	/**
	 * @param token the customer token
	 */
	public LogglyAppender(String token, String tags) {
		String _url = LogglyAppender.BASE_URL + token;
		if ((tags != null) && !tags.isEmpty()) {
			_url += "/tag/" + tags;
		}
		this.url = _url + "/";
		this.executor.execute(new Runnable() {
			
			@Override
			public void run() {
				while (!LogglyAppender.this.closed) {
					try {
						LoggingEvent event = LogglyAppender.this.eventQueue.poll(5, TimeUnit.SECONDS);
						if (event != null) {
							String json = LogglyAppender.this.createJSON(event);
							HttpResponse post = WS.url(LogglyAppender.this.url).contentType("application/json").body(json).post();
							if (post.getStatusLine().getStatusCode() != 200) {
								System.err.println("Failed to log to loggly");
							}
						}
					} catch (Exception e) {
						System.err.println("Failed to log to loggly");
					}
				}
			}
		});
	}
	
	@Override
	public void close() {
		//
	}
	
	@Override
	public boolean requiresLayout() {
		return false;
	}
	
	@Override
	protected void append(LoggingEvent event) {
		try {
			this.eventQueue.put(event);
		} catch (InterruptedException e) {
			System.err.println("Failed to append event");
		}
	}
	
	private String createJSON(LoggingEvent event) {
		Map<String, Object> log = new HashMap<>();
		log.put("daemon", DaemonStarter.getDaemonName());
		log.put("instance", DaemonStarter.getInstanceId());
		log.put("host", DaemonStarter.getHostname());
		log.put("phase", DaemonStarter.getCurrentPhase().name());
		log.put("timestamp", new Date(event.getTimeStamp()).toString());
		log.put("level", event.getLevel().toString());
		log.put("source", event.getLoggerName());
		log.put("message", event.getRenderedMessage());
		
		if (event.getThrowableInformation() != null) {
			String[] throwableStrRep = event.getThrowableInformation().getThrowableStrRep();
			log.put("stacktrace", throwableStrRep);
		}
		if (event.getProperties() != null) {
			log.put("mdc", event.getProperties());
		}
		StringBuilder sb = new StringBuilder();
		this.addObject(sb, log);
		return sb.toString();
	}
	
	private void addObject(StringBuilder sb, Map<String, Object> map) {
		sb.append("{");
		Set<Entry<String, Object>> entrySet = map.entrySet();
		boolean first = true;
		for (Entry<String, Object> entry : entrySet) {
			if (first) {
				first = false;
			} else {
				this.addSeparator(sb);
			}
			this.addField(sb, entry.getKey(), entry.getValue());
		}
		sb.append("}");
	}
	
	private void addField(StringBuilder sb, String field, Object value) {
		// Add field name
		sb.append("\"");
		sb.append(this.cleanString(field));
		sb.append("\":");
		
		this.addValue(sb, value);
	}
	
	private void addSeparator(StringBuilder sb) {
		sb.append(",");
	}
	
	private void addArray(StringBuilder sb, Object... values) {
		sb.append("[");
		for (int i = 0; i < values.length; i++) {
			this.addValue(sb, values[i]);
			if (i != (values.length - 1)) {
				this.addSeparator(sb);
			}
		}
		sb.append("]");
		
	}
	
	@SuppressWarnings("unchecked")
	private void addValue(StringBuilder sb, Object value) {
		if (value instanceof String) {
			sb.append("\"");
			sb.append(this.cleanString((String) value));
			sb.append("\"");
		} else if (value instanceof Map) {
			this.addObject(sb, (Map<String, Object>) value);
		} else if (value instanceof Object[]) {
			this.addArray(sb, (Object[]) value);
		} else {
			throw new RuntimeException("Invalid value: " + value);
		}
	}
	
	private String cleanString(String value) {
		return value.replaceAll("\"", "");
	}
	
}
