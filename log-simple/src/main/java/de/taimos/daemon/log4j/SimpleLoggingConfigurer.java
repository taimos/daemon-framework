package de.taimos.daemon.log4j;

import java.lang.reflect.Method;

import org.slf4j.impl.SimpleLogger;

import de.taimos.daemon.DaemonProperties;
import de.taimos.daemon.ILoggingConfigurer;

public class SimpleLoggingConfigurer implements ILoggingConfigurer {
	
	@Override
	public void initializeLogging() throws Exception {
		System.setProperty(SimpleLogger.LEVEL_IN_BRACKETS_KEY, "true");
		System.setProperty(SimpleLogger.LOG_FILE_KEY, "System.out");
		System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "true");
		System.setProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, "yyyy-MM-dd HH:mm:ss.SSS");
		
		// reset settings of SimpleLogger
		Method initMethod = SimpleLogger.class.getDeclaredMethod("init");
		if (initMethod != null) {
			initMethod.setAccessible(true);
			initMethod.invoke(null);
		}
	}
	
	@Override
	public void reconfigureLogging() throws Exception {
		//
	}
	
	public static void setup() {
		System.setProperty(DaemonProperties.LOGGER_CONFIGURER, SimpleLoggingConfigurer.class.getCanonicalName());
	}
	
}
