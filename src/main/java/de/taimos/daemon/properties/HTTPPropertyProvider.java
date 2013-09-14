package de.taimos.daemon.properties;

import java.io.InputStream;
import java.net.URL;

public class HTTPPropertyProvider extends StreamPropertyProvider {
	
	private final String url;
	
	
	/**
	 * @param url the HTTP URL
	 */
	public HTTPPropertyProvider(String url) {
		this.url = url;
	}
	
	@Override
	protected InputStream getStream() throws Exception {
		this.logger.info("Loading properties from: " + this.url);
		return new URL(this.url).openConnection().getInputStream();
	}
	
}
