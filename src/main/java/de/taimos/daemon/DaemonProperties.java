package de.taimos.daemon;

import org.apache.log4j.FileAppender;

/**
 * @author hoegertn
 * 
 */
public interface DaemonProperties {
	
	/** daemon is in dev mode (true/false) */
	public static final String DEVELOPMENT_MODE = "developmentMode";
	
	/** the logger level (see {@link Level}) */
	public static final String LOGGER_LEVEL = "logger.level";
	
	/** true to use {@link FileAppender}; false to disable */
	public static final String LOGGER_FILE = "logger.file";
	
	/** the log level for syslog (see {@link Level}) */
	public static final String SYSLOG_LEVEL = "syslog.level";
	
	/** the syslog facility (LOCAL0, LOCAL1, ...) */
	public static final String SYSLOG_FACILITY = "syslog.facility";
	
	/** the host for remote syslog */
	public static final String SYSLOG_HOST = "syslog.host";
	
	/** the name of the daemon */
	public static final String DAEMON_NAME = "daemonName";
	/** the name of the daemon */
	public static final String SERVICE_NAME = "serviceName";
	
	/** the property source type (aws, cs, file, http) */
	public static final String PROPERTY_SOURCE = "property.source";
	/** the property location (file name or URL) */
	public static final String PROPERTY_LOCATION = "property.location";
	/** the config server URL if property.source=cs */
	public static final String PROPERTY_SERVER = "property.server";
	/** the config server template if property.source=cs */
	public static final String PROPERTY_TEMPLATE = "property.template";
	
	/** the property source type - Amazon Web Services UserData */
	public static final String PROPERTY_SOURCE_AWS = "aws";
	/** the property source type - local file */
	public static final String PROPERTY_SOURCE_FILE = "file";
	/** the property source type - HTTP GET resource */
	public static final String PROPERTY_SOURCE_HTTP = "http";
	/** the property source type - Cinovo Config Server */
	public static final String PROPERTY_SOURCE_CS = "cs";
}
