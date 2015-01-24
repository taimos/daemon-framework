package de.taimos.daemon;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import de.taimos.daemon.log4j.SimpleLoggingConfigurer;

public class SimpleLogTest extends DaemonLifecycleAdapter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLogTest.class);
	
	
	public static void main(String[] args) {
		System.setProperty(DaemonProperties.STARTUP_MODE, DaemonProperties.STARTUP_MODE_RUN);
		SimpleLoggingConfigurer.setup();
		DaemonStarter.startDaemon("foobar", new SimpleLogTest());
		
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
		
		SimpleLogTest.LOGGER.debug("Debug");
		SimpleLogTest.LOGGER.info("Message");
		SimpleLogTest.LOGGER.warn("Warning", new RuntimeException("Failed"));
		
		MDC.put("requestID", UUID.randomUUID().toString());
		SimpleLogTest.LOGGER.info("Request");
		MDC.remove("requestID");
	}
	
}
