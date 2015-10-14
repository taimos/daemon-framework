package de.taimos.daemon.log4j;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.net.SyslogAppender;

import de.taimos.daemon.DaemonProperties;
import de.taimos.daemon.DaemonStarter;
import de.taimos.daemon.ILoggingConfigurer;

public class Log4jLoggingConfigurer implements ILoggingConfigurer {
	
	private final Logger rlog = Logger.getRootLogger();
	
	private SyslogAppender syslog;
	private DailyRollingFileAppender darofi;
	private LogglyAppender loggly;
	private LogentriesAppender logentries;
	private ConsoleAppender console;
	
	
	@Override
	public void initializeLogging() throws Exception {
		// Clear all existing appenders
		this.rlog.removeAllAppenders();
		
		this.rlog.setLevel(Level.INFO);
		
		// only use SYSLOG and DAROFI in production mode
		if (!DaemonStarter.isDevelopmentMode()) {
			this.darofi = new DailyRollingFileAppender();
			this.darofi.setName("DAROFI");
			this.darofi.setLayout(new PatternLayout("%d{HH:mm:ss,SSS} %-5p %c %x - %m%n"));
			this.darofi.setFile("log/" + DaemonStarter.getDaemonName() + ".log");
			this.darofi.setDatePattern("'.'yyyy-MM-dd");
			this.darofi.setAppend(true);
			this.darofi.setThreshold(Level.INFO);
			this.darofi.activateOptions();
			this.rlog.addAppender(this.darofi);
			
			this.syslog = new SyslogAppender();
			this.syslog.setName("SYSLOG");
			this.syslog.setLayout(new PatternLayout(DaemonStarter.getDaemonName() + ": %-5p %c %x - %m%n"));
			this.syslog.setSyslogHost("localhost");
			this.syslog.setFacility("LOCAL0");
			this.syslog.setFacilityPrinting(false);
			this.syslog.setThreshold(Level.INFO);
			this.syslog.activateOptions();
			this.rlog.addAppender(this.syslog);
		}
		if (DaemonStarter.isDevelopmentMode() || DaemonStarter.isRunMode()) {
			// CONSOLE is only active in development and run mode
			this.console = new ConsoleAppender();
			this.console.setName("CONSOLE");
			this.console.setLayout(new PatternLayout("%d{HH:mm:ss,SSS} %-5p %c %x - %m%n"));
			this.console.setTarget(ConsoleAppender.SYSTEM_OUT);
			this.console.activateOptions();
			this.rlog.addAppender(this.console);
		}
	}
	
	@Override
	public void reconfigureLogging() throws Exception {
		final Level logLevel = Level.toLevel(DaemonStarter.getDaemonProperties().getProperty(Log4jDaemonProperties.LOGGER_LEVEL), Level.INFO);
		final String logPattern = System.getProperty(Log4jDaemonProperties.LOGGER_PATTERN, "%d{HH:mm:ss,SSS} %-5p %c %x - %m%n");
		this.rlog.setLevel(logLevel);
		this.rlog.info(String.format("Changed the the log level to %s", logLevel));
		
		if (!DaemonStarter.isDevelopmentMode()) {
			final String fileEnabled = DaemonStarter.getDaemonProperties().getProperty(Log4jDaemonProperties.LOGGER_FILE, "true");
			final String syslogEnabled = DaemonStarter.getDaemonProperties().getProperty(Log4jDaemonProperties.LOGGER_SYSLOG, "true");
			final String logglyEnabled = DaemonStarter.getDaemonProperties().getProperty(Log4jDaemonProperties.LOGGER_LOGGLY, "false");
			final String logentriesEnabled = DaemonStarter.getDaemonProperties().getProperty(Log4jDaemonProperties.LOGGER_LOGENTRIES, "false");
			
			if ((fileEnabled != null) && fileEnabled.equals("false")) {
				this.rlog.removeAppender(this.darofi);
				this.darofi = null;
				this.rlog.info(String.format("Deactivated the FILE Appender"));
			} else {
				this.darofi.setThreshold(logLevel);
				this.darofi.setLayout(new PatternLayout(logPattern));
				this.darofi.activateOptions();
			}
			
			if ((syslogEnabled != null) && syslogEnabled.equals("false")) {
				this.rlog.removeAppender(this.syslog);
				this.syslog = null;
				this.rlog.info(String.format("Deactivated the SYSLOG Appender"));
			} else {
				final String host = DaemonStarter.getDaemonProperties().getProperty(Log4jDaemonProperties.SYSLOG_HOST, "localhost");
				final String facility = DaemonStarter.getDaemonProperties().getProperty(Log4jDaemonProperties.SYSLOG_FACILITY, "LOCAL0");
				final Level syslogLevel = Level.toLevel(DaemonStarter.getDaemonProperties().getProperty(Log4jDaemonProperties.SYSLOG_LEVEL), Level.INFO);
				
				this.syslog.setSyslogHost(host);
				this.syslog.setFacility(facility);
				this.syslog.setThreshold(syslogLevel);
				this.syslog.activateOptions();
				this.rlog.info(String.format("Changed the SYSLOG Appender to host %s and facility %s", host, facility));
			}
			
			if ((logglyEnabled != null) && logglyEnabled.equals("false")) {
				this.loggly = null;
				this.rlog.info(String.format("Deactivated the LOGGLY Appender"));
			} else {
				final String token = DaemonStarter.getDaemonProperties().getProperty(Log4jDaemonProperties.LOGGLY_TOKEN);
				if ((token == null) || token.isEmpty()) {
					this.rlog.error("Missing loggly token but loggly is activated");
				} else {
					final String tags = DaemonStarter.getDaemonProperties().getProperty(Log4jDaemonProperties.LOGGLY_TAGS);
					this.loggly = new LogglyAppender();
					this.loggly.setToken(token);
					this.loggly.setTags(tags);
					this.loggly.setLayout(new JSONLayout());
					this.loggly.activateOptions();
					this.rlog.addAppender(this.loggly);
				}
			}
			
			if ((logentriesEnabled != null) && logentriesEnabled.equals("false")) {
				this.logentries = null;
				this.rlog.info(String.format("Deactivated the LOGENTRIES Appender"));
			} else {
				final String token = DaemonStarter.getDaemonProperties().getProperty(Log4jDaemonProperties.LOGENTRIES_TOKEN);
				if ((token == null) || token.isEmpty()) {
					this.rlog.error("Missing logentries token but logentries is activated");
				} else {
					this.logentries = new LogentriesAppender();
					this.logentries.setToken(token);
					this.logentries.setLayout(new JSONLayout());
					this.logentries.activateOptions();
					this.rlog.addAppender(this.logentries);
				}
			}
		}
		if (DaemonStarter.isDevelopmentMode() || DaemonStarter.isRunMode()) {
			this.console.setLayout(new PatternLayout(logPattern));
			this.console.setThreshold(logLevel);
			this.console.activateOptions();
		}
	}
	
	@Override
	public void simpleLogging() throws Exception {
		// Clear all existing appenders
		this.rlog.removeAllAppenders();
		this.rlog.setLevel(Level.INFO);
		
		this.console = new ConsoleAppender();
		this.console.setName("CONSOLE");
		this.console.setLayout(new PatternLayout("%d{HH:mm:ss,SSS} %-5p %c %x - %m%n"));
		this.console.setTarget(ConsoleAppender.SYSTEM_OUT);
		this.console.activateOptions();
		this.rlog.addAppender(this.console);
	}
	
	public static void setup() {
		System.setProperty(DaemonProperties.LOGGER_CONFIGURER, Log4jLoggingConfigurer.class.getCanonicalName());
	}
	
}
