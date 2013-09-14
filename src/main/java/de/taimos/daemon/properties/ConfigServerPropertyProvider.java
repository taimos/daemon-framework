package de.taimos.daemon.properties;

import de.taimos.daemon.DaemonStarter;

public class ConfigServerPropertyProvider extends HTTPPropertyProvider {
	
	public ConfigServerPropertyProvider(String server, String template) {
		super("http://" + server + "/api/config/" + template + "/" + DaemonStarter.getDaemonName());
	}
	
}
