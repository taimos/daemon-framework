package de.taimos.daemon.properties;

import java.io.FileInputStream;
import java.io.InputStream;

public class FilePropertyProvider extends StreamPropertyProvider {
	
	private final String filename;
	
	
	/**
	 * @param filename the file name
	 */
	public FilePropertyProvider(String filename) {
		this.filename = filename;
	}
	
	@Override
	protected InputStream getStream() throws Exception {
		this.logger.info("Loading properties from: " + this.filename);
		return new FileInputStream(this.filename);
	}
	
}
