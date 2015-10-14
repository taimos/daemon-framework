package de.taimos.daemon.log4j;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import de.taimos.httputils.WS;

/**
 * Appender for the Loggly log service
 * 
 * @author hoegertn
 * 
 */
public class LogglyAppender extends AppenderSkeleton {
	
	private static final String BASE_URL = "https://logs-01.loggly.com/inputs/";
	
	private String token;
	private String tags;
	private String url;
	
	private LinkedBlockingQueue<String> eventQueue = new LinkedBlockingQueue<>();
	
	private Executor executor = Executors.newSingleThreadExecutor();
	
	
	/**
	 * 
	 */
	public LogglyAppender() {
		this.executor.execute(new Runnable() {
			
			@Override
			public void run() {
				while (!LogglyAppender.this.closed) {
					try {
						String json = LogglyAppender.this.eventQueue.poll(5, TimeUnit.SECONDS);
						if (json != null) {
							HttpResponse post = WS.url(LogglyAppender.this.url).timeout(10000).contentType("application/json").body(json).post();
							if (post.getStatusLine().getStatusCode() != 200) {
								System.err.println("Failed to log to loggly");
							}
						}
					} catch (Exception e) {
						System.err.println("Failed to log to loggly");
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public String getToken() {
		return this.token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getTags() {
		return this.tags;
	}
	
	public void setTags(String tags) {
		this.tags = tags;
	}
	
	@Override
	public void close() {
		this.closed = true;
	}
	
	@Override
	public boolean requiresLayout() {
		return true;
	}
	
	@Override
	public void activateOptions() {
		String _url = LogglyAppender.BASE_URL + this.token;
		if ((this.tags != null) && !this.tags.isEmpty()) {
			_url += "/tag/" + this.tags;
		}
		this.url = _url + "/";
	}
	
	@Override
	protected void append(LoggingEvent event) {
		try {
			String log = this.layout.format(event);
			if (this.layout.ignoresThrowable()) {
				// add throwable info
				StringBuilder sb = new StringBuilder(log);
				String[] throwableStrRep = event.getThrowableStrRep();
				for (String stack : throwableStrRep) {
					sb.append("\n");
					sb.append(stack);
				}
				log = sb.toString();
			}
			this.eventQueue.put(log);
		} catch (InterruptedException e) {
			System.err.println("Failed to append event");
		}
	}
	
}
