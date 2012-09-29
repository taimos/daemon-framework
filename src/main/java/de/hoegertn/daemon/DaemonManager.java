package de.hoegertn.daemon;

/**
 * 
 * @author hoegertn
 * 
 */
class DaemonManager {

	private final Object mutex = new Object();

	private boolean running = true;

	/**
	 * blocks until stop() is called
	 */
	void block() {
		while (this.isRunning()) {
			synchronized (this.mutex) {
				try {
					this.mutex.wait();
				} catch (final Exception e) {
					// ignore it and wait again
				}
			}
		}
	}

	/**
	 * @return is the daemon running
	 */
	boolean isRunning() {
		return this.running;
	}

	/**
	 * stop the daemon
	 */
	void stop() {
		synchronized (this.mutex) {
			this.running = false;
			this.mutex.notifyAll();
		}
	}
}
