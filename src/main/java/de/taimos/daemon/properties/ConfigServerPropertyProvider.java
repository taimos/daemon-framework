package de.taimos.daemon.properties;

import org.apache.http.HttpResponse;

import de.taimos.daemon.DaemonStarter;
import de.taimos.httputils.HTTPRequest;
import de.taimos.httputils.WS;

public class ConfigServerPropertyProvider extends HTTPPropertyProvider {
	
	private String server;
	private String template;
	
	
	public ConfigServerPropertyProvider(String server, String template) {
		this.server = server;
		this.template = template;
	}
	
	@Override
	protected String getDescription() {
		return String.format("Config Server %s with template %s", this.server, this.template);
	}
	
	@Override
	protected HttpResponse getResponse() {
		HTTPRequest req = WS.url("http://" + this.server + "/api/config/{template}/{svc}");
		req.pathParam("template", this.template).pathParam("svc", DaemonStarter.getDaemonName());
		req.accept("application/x-javaprops");
		return req.get();
	}
	
}
