package de.taimos.daemon;

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
	
	/** the ttl for dns timeouts */
	public static final String DNS_TTL = "dns.ttl";
	
	/** the clazz of the {@link ILoggingConfigurer} */
	public static final String LOGGER_CONFIGURER = "loggerConfigurer";
	
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
