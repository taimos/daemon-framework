package de.taimos.daemon;

/*
 * #%L
 * Daemon Library Log4j extension
 * %%
 * Copyright (C) 2012 - 2016 Taimos GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import de.taimos.daemon.log4j.Log4jDaemonProperties;
import de.taimos.daemon.log4j.Log4jLoggingConfigurer;

public class LogglyTest extends DaemonLifecycleAdapter {
	
	private static final String TOKEN = ""; // TODO Fill in
	private static final Logger log = Logger.getRootLogger();
	
	
	public static void main(String[] args) {
		System.setProperty(DaemonProperties.STARTUP_MODE, DaemonProperties.STARTUP_MODE_RUN);
		Log4jLoggingConfigurer.setup();
		DaemonStarter.startDaemon("foobar", new LogglyTest());
		
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
		
		LogglyTest.log.info("Message");
		LogglyTest.log.warn("Warning", new RuntimeException("Failed"));
		
		MDC.put("requestID", UUID.randomUUID().toString());
		LogglyTest.log.info("Request");
		MDC.remove("requestID");
	}
	
	@Override
	public Map<String, String> loadProperties() {
		Map<String, String> prop = new HashMap<>();
		prop.put(Log4jDaemonProperties.LOGGER_LOGENTRIES, "false");
		prop.put(Log4jDaemonProperties.LOGGER_LOGGLY, "true");
		prop.put(Log4jDaemonProperties.LOGGER_SYSLOG, "false");
		
		prop.put(Log4jDaemonProperties.LOGGLY_TOKEN, LogglyTest.TOKEN);
		prop.put(Log4jDaemonProperties.LOGGLY_TAGS, "foo,bar");
		return prop;
	}
}
