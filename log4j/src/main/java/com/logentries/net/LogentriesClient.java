package com.logentries.net;

/*
 * #%L
 * Daemon Library Log4j extension
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

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

/**
 * Client for sending messages to Logentries via HTTP PUT or Token-Based Logging
 * Supports SSL/TLS
 * 
 * @author Mark Lacomber
 * 
 */
public class LogentriesClient {
	
	/** Logentries API server address for Token-based input. */
	private static final String LE_API = "api.logentries.com";
	/** Port number for SSL/TLS Token TCP logging on Logentries server. */
	private static final int LE_SSL_PORT = 20000;
	
	final SSLSocketFactory ssl_factory;
	private Socket socket;
	private OutputStream stream;
	
	
	public LogentriesClient() {
		this.ssl_factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
	}
	
	public int getPort() {
		return LogentriesClient.LE_SSL_PORT;
	}
	
	public String getAddress() {
		return LogentriesClient.LE_API;
	}
	
	public void connect() throws IOException {
		this.socket = SSLSocketFactory.getDefault().createSocket(this.getAddress(), this.getPort());
		this.stream = this.socket.getOutputStream();
	}
	
	public void write(byte[] buffer, int offset, int length) throws IOException {
		if (this.stream == null) {
			throw new IOException();
		}
		this.stream.write(buffer, offset, length);
		this.stream.flush();
	}
	
	public void close() {
		try {
			if (this.socket != null) {
				this.socket.close();
				this.socket = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
