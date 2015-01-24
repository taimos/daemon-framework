package de.taimos.daemon.properties;

import java.io.InputStream;

import org.apache.http.HttpResponse;

public abstract class HTTPPropertyProvider extends StreamPropertyProvider {
	
	@Override
	protected InputStream getStream() throws Exception {
		this.logger.info("Loading properties from: " + this.getDescription());
		HttpResponse res = this.getResponse();
		return res.getEntity().getContent();
	}
	
	protected abstract String getDescription();
	
	protected abstract HttpResponse getResponse();
}
