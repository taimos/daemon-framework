package de.taimos.daemon.log4j;

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

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.logentries.net.AsyncLogger;

/**
 * Logentries appender for log4j.
 * 
 * @author Mark Lacomber
 * 
 */
public class LogentriesAppender extends AppenderSkeleton {
	
	/*
	 * Fields
	 */
	/** Asynchronous Background logger */
	AsyncLogger le_async;
	
	
	public LogentriesAppender() {
		this.le_async = new AsyncLogger();
	}
	
	/*
	 * Public methods to send log4j parameters to AsyncLogger
	 */
	/**
	 * Sets the token
	 * 
	 * @param token the logentries token
	 */
	public void setToken(String token) {
		this.le_async.setToken(token);
	}
	
	/**
	 * Sets the debug flag. Appender in debug mode will print error messages on
	 * error console.
	 * 
	 * @param debug debug flag to set
	 */
	public void setDebug(boolean debug) {
		this.le_async.setDebug(debug);
	}
	
	/**
	 * Determines whether to send HostName alongside with the log message
	 *
	 * @param logHostName true to log host name
	 */
	public void setLogHostName(boolean logHostName) {
		this.le_async.setLogHostName(logHostName);
	}
	
	/**
	 * Sets the HostName from the configuration
	 *
	 * @param hostName the host name
	 */
	public void setHostName(String hostName) {
		this.le_async.setHostName(hostName);
	}
	
	/**
	 * Sets LogID parameter from the configuration
	 *
	 * @param logID the log id
	 */
	public void setLogID(String logID) {
		this.le_async.setLogID(logID);
	}
	
	/**
	 * Implements AppenderSkeleton Append method, handles time and format
	 * 
	 * @param event event to log
	 */
	@Override
	protected void append(LoggingEvent event) {
		
		// Render the event according to layout
		String formattedEvent = this.layout.format(event);
		
		// Append stack trace if present and layout does not handle it
		if (this.layout.ignoresThrowable()) {
			String[] stack = event.getThrowableStrRep();
			if (stack != null) {
				int len = stack.length;
				formattedEvent += ", ";
				for (int i = 0; i < len; i++) {
					formattedEvent += stack[i];
					if (i < (len - 1)) {
						formattedEvent += "\u2028";
					}
				}
			}
		}
		
		// Prepare to be queued
		this.le_async.addLineToQueue(formattedEvent);
	}
	
	/**
	 * Closes all connections to Logentries
	 */
	@Override
	public void close() {
		this.le_async.close();
	}
	
	@Override
	public boolean requiresLayout() {
		return true;
	}
}
