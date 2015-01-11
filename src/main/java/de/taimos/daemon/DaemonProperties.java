package de.taimos.daemon;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.net.SyslogAppender;

/**
 * @author hoegertn
 *
 */
public interface DaemonProperties {
	
	/** daemon startup mode (dev|start|run) */
	public static final String STARTUP_MODE = "startupMode";
	/** daemon startup mode - Development */
	public static final String STARTUP_MODE_DEV = "dev";
	/** daemon startup mode - Start (Daemon) */
	public static final String STARTUP_MODE_START = "start";
	/** daemon startup mode - Run (Foreground) */
	public static final String STARTUP_MODE_RUN = "run";
	
	/** daemon is in dev mode (true/false) */
	@Deprecated
	public static final String DEVELOPMENT_MODE = "developmentMode";
	
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
	
	/** the name of the daemon */
	public static final String DAEMON_NAME = "daemonName";
	/** the name of the daemon */
	public static final String SERVICE_NAME = "serviceName";
	
	/** the property source type (aws, c2, file, http) */
	public static final String PROPERTY_SOURCE = "property.source";
	/** the property location (file name or URL) */
	public static final String PROPERTY_LOCATION = "property.location";
	/** the CloudConductor server URL if property.source=c2 */
	public static final String PROPERTY_SERVER = "property.server";
	/** the CloudConductor template if property.source=c2 */
	public static final String PROPERTY_TEMPLATE = "property.template";
	
	/** the property source type - Amazon Web Services UserData */
	public static final String PROPERTY_SOURCE_AWS = "aws";
	/** the property source type - local file */
	public static final String PROPERTY_SOURCE_FILE = "file";
	/** the property source type - HTTP GET resource */
	public static final String PROPERTY_SOURCE_HTTP = "http";
	/** the property source type - Cinovo CloudConductor Server */
	public static final String PROPERTY_SOURCE_C2 = "c2";
}
