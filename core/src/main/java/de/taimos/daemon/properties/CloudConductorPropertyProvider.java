package de.taimos.daemon.properties;

/*
 * #%L
 * Daemon Library
 * %%
 * Copyright (C) 2012 - 2016 Taimos GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.apache.http.HttpResponse;

import de.taimos.daemon.DaemonStarter;
import de.taimos.httputils.HTTPRequest;
import de.taimos.httputils.WS;

public class CloudConductorPropertyProvider extends HTTPPropertyProvider {
	
	public static final String CLOUDCONDUCTOR_URL = "CLOUDCONDUCTOR_URL";
	public static final String TEMPLATE_NAME = "TEMPLATE_NAME";
	
	private String server;
	private String template;
	
	
	public CloudConductorPropertyProvider() {
		this(System.getenv(CLOUDCONDUCTOR_URL), System.getenv(TEMPLATE_NAME));
	}

	public CloudConductorPropertyProvider(String server, String template) {
		this.server = server;
		this.template = template;
	}
	
	@Override
	protected String getDescription() {
		return String.format("CloudConductor Server %s with template %s", this.server, this.template);
	}
	
	@Override
	protected HttpResponse getResponse() {
		HTTPRequest req = WS.url("http://" + this.server + "/api/config/{template}/{svc}");
		req.pathParam("template", this.template).pathParam("svc", DaemonStarter.getDaemonName());
		req.accept("application/x-javaprops");
		return req.get();
	}
	
}
