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

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.net.SyslogAppender;

/**
 * @author hoegertn
 *
 */
public interface Log4jDaemonProperties {
	
	/** the logger level (see {@link Level}) */
	String LOGGER_LEVEL = "logger.level";
	/** the logger pattern */
	String LOGGER_PATTERN = "logger.pattern";
	
	/** true to use {@link FileAppender}; false to disable */
	String LOGGER_FILE = "logger.file";
	/** true to use {@link SyslogAppender}; false to disable */
	String LOGGER_SYSLOG = "logger.syslog";
	/** true to use {@link LogglyAppender}; false to disable */
	String LOGGER_LOGGLY = "logger.loggly";
	/** true to use {@link LogglyAppender}; false to disable */
	String LOGGER_LOGENTRIES = "logger.logentries";
	/** true to use SumoLogicAppender; false to disable */
	String LOGGER_SUMOLOGIC = "logger.sumologic";
	
	/** the log level for syslog (see {@link Level}) */
	String SYSLOG_LEVEL = "syslog.level";
	/** the syslog facility (LOCAL0, LOCAL1, ...) */
	String SYSLOG_FACILITY = "syslog.facility";
	/** the host for remote syslog */
	String SYSLOG_HOST = "syslog.host";
	
	/** the customer token for loggly */
	String LOGGLY_TOKEN = "loggly.token";
	/** the tags for loggly */
	String LOGGLY_TAGS = "loggly.tags";
	
	/** the log token for logentries.com */
	String LOGENTRIES_TOKEN = "logentries.token";

	/** the log URL for sumologic.com HTTP endpoint */
	String SUMOLOGIC_URL = "sumologic.url";
	
}
