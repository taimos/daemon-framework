package de.taimos.daemon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

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

	/**
	 * @param filename
	 * @return the map containing the properties
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static Map<String, String> loadPropertiesFile(final String filename) throws FileNotFoundException, IOException {
		final HashMap<String, String> map = new HashMap<>();
		try (FileReader fr = new FileReader(filename)) {
			final Properties prop = new Properties();
			prop.load(fr);
			for (final Entry<Object, Object> entry : prop.entrySet()) {
				map.put(entry.getKey().toString(), entry.getValue().toString());
			}
		}
		return map;
	}

}
