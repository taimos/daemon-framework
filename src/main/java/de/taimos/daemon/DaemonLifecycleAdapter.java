package de.taimos.daemon;

import java.util.Map;

import de.taimos.daemon.properties.CloudConductorPropertyProvider;
import de.taimos.daemon.properties.EmptyPropertyProvider;
import de.taimos.daemon.properties.FilePropertyProvider;
import de.taimos.daemon.properties.IPropertyProvider;
import de.taimos.daemon.properties.SimpleHTTPPropertyProvider;
import de.taimos.daemon.properties.UserDataPropertyProvider;

/**
 * Adapter for {@link IDaemonLifecycleListener}
 *
 * @author hoegertn
 *
 */
public class DaemonLifecycleAdapter implements IDaemonLifecycleListener {
	
	@Override
	public void doStart() throws Exception {
		// override in subclass when needed
	}
	
	@Override
	public void doStop() throws Exception {
		// override in subclass when needed
	}
	
	@Override
	public void started() {
		// override in subclass when needed
	}
	
	@Override
	public void stopped() {
		// override in subclass when needed
	}
	
	@Override
	public void stopping() {
		// override in subclass when needed
	}
	
	@Override
	public void aborting() {
		// override in subclass when needed
	}
	
	@Override
	public void signalUSR2() {
		// override in subclass when needed
	}
	
	@Override
	public void exception(final LifecyclePhase phase, final Throwable exception) {
		// override in subclass when needed
		System.err.println("Exception in phase: " + phase.name());
		exception.printStackTrace();
	}
	
	@Override
	public Map<String, String> loadProperties() {
		return this.getPropertyProvider().loadProperties();
	}
	
	@Override
	public int getShutdownTimeoutSeconds() {
		return 10;
	}
	
	/**
	 * @return the property provider
	 */
	public IPropertyProvider getPropertyProvider() {
		switch (System.getProperty(DaemonProperties.PROPERTY_SOURCE, "")) {
		case DaemonProperties.PROPERTY_SOURCE_AWS:
			return new UserDataPropertyProvider();
		case DaemonProperties.PROPERTY_SOURCE_FILE:
			return new FilePropertyProvider(System.getProperty(DaemonProperties.PROPERTY_LOCATION));
		case DaemonProperties.PROPERTY_SOURCE_C2:
			return new CloudConductorPropertyProvider(System.getProperty(DaemonProperties.PROPERTY_SERVER), System.getProperty(DaemonProperties.PROPERTY_TEMPLATE));
		case DaemonProperties.PROPERTY_SOURCE_HTTP:
			return new SimpleHTTPPropertyProvider(System.getProperty(DaemonProperties.PROPERTY_LOCATION));
		default:
			return new EmptyPropertyProvider();
		}
	}
	
}
