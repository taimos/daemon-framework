package de.taimos.daemon.properties;

import java.util.HashMap;
import java.util.Map;

public class EmptyPropertyProvider implements IPropertyProvider {
	
	@Override
	public Map<String, String> loadProperties() {
		return new HashMap<>();
	}
	
}
