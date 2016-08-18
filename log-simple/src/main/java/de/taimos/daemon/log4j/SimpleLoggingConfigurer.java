package de.taimos.daemon.log4j;

/*
 * #%L
 * Daemon Library Simple Log extension
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
	
	@Override
	public void simpleLogging() throws Exception {
		this.initializeLogging();
	}
	
	public static void setup() {
		System.setProperty(DaemonProperties.LOGGER_CONFIGURER, SimpleLoggingConfigurer.class.getCanonicalName());
	}
	
}
