package de.hoegertn.daemon;

import java.util.Map;

/**
 * Listener for the lifecycle of a system daemon
 * 
 * @author hoegertn
 * 
 */
public interface IDaemonLifecycleListener {

	/**
	 * Will be called to allow for custom startup code
	 * 
	 * @return true if start was successful
	 */
	boolean doStart();

	/**
	 * Will be called to allow for custom shutdown code
	 * 
	 * @return true if shutdown was successful
	 */
	boolean doStop();

	/**
	 * will be called after successful startup
	 */
	void started();

	/**
	 * will be called after successful shutdown
	 */
	void stopped();

	/**
	 * will be called on imminent shutdown
	 */
	void stopping();

	/**
	 * will be called on imminent abortion
	 */
	void aborting();

	/**
	 * received custom signal SIGUSR2
	 */
	void signalUSR2();

	/**
	 * This method is called if an error occurs. It provides the current {@link LifecyclePhase} and the exception
	 * 
	 * @param phase
	 *            the phase the error occured in
	 * @param exception
	 *            the occured exception
	 */
	void exception(LifecyclePhase phase, Throwable exception);

	/**
	 * @return the map of properties
	 */
	Map<String, String> loadProperties();

}
