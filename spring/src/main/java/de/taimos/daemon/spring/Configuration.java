package de.taimos.daemon.spring;

/*
 * #%L Daemon with Spring and CXF %% Copyright (C) 2013 Taimos GmbH %% Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License. #L%
 */

public interface Configuration {
	
	public static final String PROFILES = "profiles";
	
	public static final String PROFILES_PRODUCTION = "prod";
	public static final String PROFILES_TEST = "test";
	
	public static final String DEFAULT_HANDLER_CLASS = "defaultHandlerClass";
	public static final String SERVICE_PACKAGE = "servicePackage";
	
	public static final String SERVICE_PORT = "svc.port";
}
