package de.taimos.daemon;

import org.apache.log4j.FileAppender;
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
	 * true to use {@link FileAppender}; false to disable
	 */
	public static final String LOGGER_FILE = "logger.file";

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

	/**
	 * the name of the daemon
	 */
	public static final String DAEMON_NAME = "daemonName";

}
