package de.taimos.daemon;

import de.taimos.daemon.DaemonStarter;

public class DaemonStopper {

	public static void main(String[] args) {
		DaemonStarter.stopService();
	}
}
