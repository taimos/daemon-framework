package de.taimos.daemon.properties;

import org.apache.http.HttpResponse;

import de.taimos.httputils.WS;

public class SimpleHTTPPropertyProvider extends HTTPPropertyProvider {
	
	private String url;
	
	
	public SimpleHTTPPropertyProvider(String url) {
		this.url = url;
	}
	
	@Override
	protected String getDescription() {
		return this.url;
	}
	
	@Override
	protected HttpResponse getResponse() {
		return WS.url(this.url).get();
	}
	
}
