package de.taimos.daemon;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.net.SyslogAppender;

/**
 * @author hoegertn
 *
 */
public interface Log4jDaemonProperties {
	
	/** the logger level (see {@link Level}) */
	public static final String LOGGER_LEVEL = "logger.level";
	/** the logger pattern */
	public static final String LOGGER_PATTERN = "logger.pattern";
	
	/** true to use {@link FileAppender}; false to disable */
	public static final String LOGGER_FILE = "logger.file";
	/** true to use {@link SyslogAppender}; false to disable */
	public static final String LOGGER_SYSLOG = "logger.syslog";
	/** true to use {@link LogglyAppender}; false to disable */
	public static final String LOGGER_LOGGLY = "logger.loggly";
	
	/** the log level for syslog (see {@link Level}) */
	public static final String SYSLOG_LEVEL = "syslog.level";
	/** the syslog facility (LOCAL0, LOCAL1, ...) */
	public static final String SYSLOG_FACILITY = "syslog.facility";
	/** the host for remote syslog */
	public static final String SYSLOG_HOST = "syslog.host";
	
	/** the customer token for loggly */
	public static final String LOGGLY_TOKEN = "loggly.token";
	/** the tags for loggly */
	public static final String LOGGLY_TAGS = "loggly.tags";
	
}
