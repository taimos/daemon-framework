package de.taimos.daemon;

/**
 * The different phases of the daemon lifecycle
 * 
 * @author hoegertn
 * 
 */
public enum LifecyclePhase {

	/**
	 * The daemon is currently starting
	 */
	STARTING,
	/**
	 * The daemon is started
	 */
	STARTED,
	/**
	 * The daemon is currently shutting down
	 */
	STOPPING,
	/**
	 * The daemon is currently aborting
	 */
	ABORTING,
	/**
	 * The daemon is stopped
	 */
	STOPPED;
}
