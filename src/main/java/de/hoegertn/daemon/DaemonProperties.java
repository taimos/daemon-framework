package de.hoegertn.daemon;

import org.apache.log4j.Level;

/**
 * @author hoegertn
 * 
 */
public interface DaemonProperties {

	/**
	 * daemon is in dev mode (true/false)
	 */
	public static final String DEVELOPMENT_MODE = "developmentMode";

	/**
	 * the logger level (see {@link Level})
	 */
	public static final String LOGGER_LEVEL = "logger.level";

	/**
	 * the log level for syslog (see {@link Level})
	 */
	public static final String SYSLOG_LEVEL = "syslog.level";

	/**
	 * the syslog facility (LOCAL0, LOCAL1, ...)
	 */
	public static final String SYSLOG_FACILITY = "syslog.facility";

	/**
	 * the host for remote syslog
	 */
	public static final String SYSLOG_HOST = "syslog.host";

}
