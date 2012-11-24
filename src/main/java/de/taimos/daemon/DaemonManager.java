package de.taimos.daemon;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * @author hoegertn
 * 
 */
class DaemonManager {

	private final Object mutex = new Object();

	private final AtomicBoolean running = new AtomicBoolean(true);

	/**
	 * blocks until stop() is called
	 */
	void block() {
		while (this.isRunning()) {
			synchronized (this.mutex) {
				try {
					if (this.isRunning()) {
						this.mutex.wait();
					}
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
		return this.running.get();
	}

	/**
	 * stop the daemon
	 */
	void stop() {
		synchronized (this.mutex) {
			this.running.set(false);
			this.mutex.notifyAll();
		}
	}
}
