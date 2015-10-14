package com.logentries.net;

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
	
	public void connect() throws UnknownHostException, IOException {
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
