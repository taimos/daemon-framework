package de.taimos.daemon;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import de.taimos.daemon.log4j.Log4jLoggingConfigurer;

public class Log4jTest extends DaemonLifecycleAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Log4jTest.class);
	
	
	public static void main(String[] args) {
		System.setProperty(DaemonProperties.STARTUP_MODE, DaemonProperties.STARTUP_MODE_RUN);
		Log4jLoggingConfigurer.setup();
		DaemonStarter.startDaemon("foobar", new Log4jTest());
		
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
		
		Log4jTest.LOGGER.info("Message");
		Log4jTest.LOGGER.warn("Warning", new RuntimeException("Failed"));
		
		MDC.put("requestID", UUID.randomUUID().toString());
		Log4jTest.LOGGER.info("Request");
		MDC.remove("requestID");
	}
	
	@Override
	public Map<String, String> loadProperties() {
		Map<String, String> prop = super.loadProperties();
		prop.put("logger.loggly", "false");
		prop.put("logger.syslog", "false");
		return prop;
	}
}
