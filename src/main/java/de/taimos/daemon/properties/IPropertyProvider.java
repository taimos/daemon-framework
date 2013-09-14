package de.taimos.daemon.properties;

import java.util.Map;

/**
 * provider for system properties
 */
public interface IPropertyProvider {
	
	/**
	 * @return the map of properties
	 */
	Map<String, String> loadProperties();
	
}
