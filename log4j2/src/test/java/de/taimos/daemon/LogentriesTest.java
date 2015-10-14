package de.taimos.daemon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import de.taimos.daemon.log4j.Log4jDaemonProperties;
import de.taimos.daemon.log4j.Log4jLoggingConfigurer;

public class LogentriesTest extends DaemonLifecycleAdapter {
	
	private static final String TOKEN = "83ec5ec1-1de9-4477-9015-bcf774caa7bc"; // TODO Fill in
	private static final Logger log = Logger.getRootLogger();
	
	
	public static void main(String[] args) {
		System.setProperty(DaemonProperties.STARTUP_MODE, DaemonProperties.STARTUP_MODE_RUN);
		Log4jLoggingConfigurer.setup();
		DaemonStarter.startDaemon("foobar", new LogentriesTest());
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		DaemonStarter.stopService();
	}
	
	@Override
	public void started() {
		super.started();
		
		LogentriesTest.log.info("Message");
		LogentriesTest.log.warn("Warning", new RuntimeException("Failed"));
		
		MDC.put("requestID", UUID.randomUUID().toString());
		LogentriesTest.log.info("Request");
		MDC.remove("requestID");
	}
	
	@Override
	public Map<String, String> loadProperties() {
		Map<String, String> prop = new HashMap<>();
		prop.put(Log4jDaemonProperties.LOGGER_LOGENTRIES, "true");
		prop.put(Log4jDaemonProperties.LOGGER_LOGGLY, "false");
		prop.put(Log4jDaemonProperties.LOGGER_SYSLOG, "false");
		
		prop.put(Log4jDaemonProperties.LOGENTRIES_TOKEN, LogentriesTest.TOKEN);
		return prop;
	}
}
