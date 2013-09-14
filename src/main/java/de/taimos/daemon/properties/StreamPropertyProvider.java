package de.taimos.daemon.properties;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StreamPropertyProvider implements IPropertyProvider {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Override
	public Map<String, String> loadProperties() {
		final HashMap<String, String> map = new HashMap<>();
		try (InputStream s = this.getStream()) {
			final Properties prop = new Properties();
			prop.load(s);
			for (final Entry<Object, Object> entry : prop.entrySet()) {
				map.put(entry.getKey().toString(), entry.getValue().toString());
			}
		} catch (Exception e) {
			this.logger.error("Failed to load properties from stream", e);
		}
		return map;
	}
	
	protected abstract InputStream getStream() throws Exception;
	
}
