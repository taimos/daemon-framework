package de.taimos.daemon;

import java.util.Map;

/**
 * Adapter for {@link IDaemonLifecycleListener}
 * 
 * @author hoegertn
 * 
 */
public class DaemonLifecycleAdapter implements IDaemonLifecycleListener {

	@Override
	public boolean doStart() {
		// override in subclass when needed
		return true;
	}

	@Override
	public boolean doStop() {
		// override in subclass when needed
		return true;
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
		return null;
	}

}
