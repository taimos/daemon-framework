package de.taimos.daemon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class LogglyTest extends DaemonLifecycleAdapter {
	
	private static final String TOKEN = ""; // TODO Fill in
	private static final Logger log = Logger.getRootLogger();
	
	
	public static void main(String[] args) {
		System.setProperty("developmentMode", "false");
		DaemonStarter.startDaemon("foobar", new LogglyTest());
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		DaemonStarter.stopService();
	}
	
	@Override
	public void started() {
		super.started();
		
		LogglyTest.log.info("Message");
		LogglyTest.log.warn("Warning", new RuntimeException("Failed"));
		
		MDC.put("requestID", UUID.randomUUID().toString());
		LogglyTest.log.info("Request");
		MDC.remove("requestID");
	}
	
	@Override
	public Map<String, String> loadProperties() {
		Map<String, String> prop = new HashMap<>();
		prop.put("logger.loggly", "true");
		prop.put("logger.syslog", "false");
		
		prop.put("loggly.token", LogglyTest.TOKEN);
		prop.put("loggly.tags", "foo,bar");
		return prop;
	}
}
