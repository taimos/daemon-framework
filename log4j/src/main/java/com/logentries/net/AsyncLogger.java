package com.logentries.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Pattern;

/**
 * Logentries Asynchronous Logger for integration with Java logging frameworks.
 *
 * a RevelOps™ service
 *
 *
 * VERSION: 1.2.0
 *
 * @author Viliam Holub
 * @author Mark Lacomber
 *
 */

public class AsyncLogger {
	
	/*
	 * Constants
	 */
	
	/** Size of the internal event queue. */
	private static final int QUEUE_SIZE = 32768;
	/** Limit on individual log length ie. 2^16 */
	public static final int LOG_LENGTH_LIMIT = 65536;
	/** Limit on recursion for appending long logs to queue */
	private static final int RECURSION_LIMIT = 32;
	/** UTF-8 output character set. */
	private static final Charset UTF8 = Charset.forName("UTF-8");
	/** Minimal delay between attempts to reconnect in milliseconds. */
	private static final int MIN_DELAY = 100;
	/** Maximal delay between attempts to reconnect in milliseconds. */
	private static final int MAX_DELAY = 10000;
	/** LE appender signature - used for debugging messages. */
	private static final String LE = "LE ";
	/** Platform dependent line separator to check for. Supported in Java 1.6+ */
	private static final String LINE_SEP = System.getProperty("line_separator", "\n");
	/** Error message displayed when invalid API key is detected. */
	private static final String INVALID_TOKEN = "\n\nIt appears your LOGENTRIES_TOKEN parameter in log4j.xml is incorrect!\n\n";
	/** Key Value for Token Environment Variable. */
	private static final String CONFIG_TOKEN = "LOGENTRIES_TOKEN";
	/** Error message displayed when queue overflow occurs */
	private static final String QUEUE_OVERFLOW = "\n\nLogentries Buffer Queue Overflow. Message Dropped!\n\n";
	/** Identifier for this client library */
	private static final String LIBRARY_ID = "###J01### - Library initialised";
	
	/** Reg.ex. that is used to check correctness of HostName if it is defined by user */
	private static final Pattern HOSTNAME_REGEX = Pattern.compile("[$/\\\"&+,:;=?#|<>_* \\[\\]]");
	
	/*
	 * Fields
	 */
	
	/** Destination Token. */
	String token = "";
	/** Debug flag. */
	boolean debug = false;
	/** Make local connection only. */
	boolean local = false;
	/** LogHostName - switch that determines whether HostName should be appended to the log message */
	boolean logHostName = false;
	/** HostName - value, that should be appended to the log message if logHostName is set to true */
	String hostName = "";
	/** LogID - user-defined ID string that is appended to the log message if non-empty */
	String logID = "";
	
	/** Indicator if the socket appender has been started. */
	boolean started = false;
	
	/** Asynchronous socket appender. */
	SocketAppender appender;
	/** Message queue. */
	ArrayBlockingQueue<String> queue;
	
	
	/*
	 * Public methods for parameters
	 */
	/**
	 * Sets the token
	 *
	 * @param token the logentries token
	 */
	public void setToken(String token) {
		this.token = token;
		this.dbg("Setting token to " + token);
	}
	
	/**
	 * Returns current token.
	 *
	 * @return current token
	 */
	public String getToken() {
		return this.token;
	}
	
	/**
	 * Sets the debug flag. Appender in debug mode will print error messages on
	 * error console.
	 *
	 * @param debug debug flag to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
		this.dbg("Setting debug to " + debug);
	}
	
	/**
	 * Returns current debug flag.
	 *
	 * @return true if debugging is enabled
	 */
	public boolean getDebug() {
		return this.debug;
	}
	
	/**
	 * Sets value of the switch that determines whether to send HostName alongside with the log message
	 *
	 * @param logHostName true to send hostname
	 */
	public void setLogHostName(boolean logHostName) {
		this.logHostName = logHostName;
	}
	
	/**
	 * Gets value of the switch that determines whether to send HostName alongside with the log message
	 *
	 * @return logHostName switch value
	 */
	public boolean getLogHostName() {
		return this.logHostName;
	}
	
	/**
	 * Sets the HostName from configuration
	 *
	 * @param hostName the hostname to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	/**
	 * Gets HostName parameter
	 *
	 * @return Host name field value
	 */
	public String getHostName() {
		return this.hostName;
	}
	
	/**
	 * Sets LogID parameter from config
	 *
	 * @param logID the log id
	 */
	public void setLogID(String logID) {
		this.logID = logID;
	}
	
	/**
	 * Gets LogID parameter
	 *
	 * @return logID field value
	 */
	public String getLogID() {
		return this.logID;
	}
	
	/**
	 * Initializes asynchronous logging.
	 *
	 * @param local make local connection to API server for testing
	 */
	AsyncLogger(boolean local) {
		this.local = local;
		
		this.queue = new ArrayBlockingQueue<String>(AsyncLogger.QUEUE_SIZE);
		// Fill the queue with an identifier message for first entry sent to server
		this.queue.offer(AsyncLogger.LIBRARY_ID);
		
		this.appender = new SocketAppender();
	}
	
	/**
	 * Initializes asynchronous logging.
	 */
	public AsyncLogger() {
		this(false);
	}
	
	/**
	 * Checks that the UUID is valid
	 */
	boolean checkValidUUID(String uuid) {
		if ("".equals(uuid)) {
			return false;
		}
		
		try {
			UUID u = UUID.fromString(uuid);
		} catch (IllegalArgumentException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Try and retrieve environment variable for given key, return empty string if not found
	 */
	
	String getEnvVar(String key) {
		String envVal = System.getenv(key);
		
		return envVal != null ? envVal : "";
	}
	
	/**
	 * Checks that key and location are set.
	 */
	boolean checkCredentials() {
		if (this.token.equals(AsyncLogger.CONFIG_TOKEN) || this.token.equals("")) {
			// Check if set in an environment variable, used with PaaS providers
			String envToken = this.getEnvVar(AsyncLogger.CONFIG_TOKEN);
			
			if (envToken == "") {
				this.dbg(AsyncLogger.INVALID_TOKEN);
				return false;
			}
			
			this.setToken(envToken);
		}
		
		return this.checkValidUUID(this.getToken());
	}
	
	/**
	 * Checks whether given host name is valid (e.g. does not contain any prohibited characters)
	 * 
	 * @param hostName - string containing host name
	 */
	boolean checkIfHostNameValid(String hostName) {
		return !AsyncLogger.HOSTNAME_REGEX.matcher(hostName).find();
	}
	
	/**
	 * Adds the data to internal queue to be sent over the network.
	 *
	 * It does not block. If the queue is full, it removes latest event first to
	 * make space.
	 *
	 * @param line line to append
	 */
	public void addLineToQueue(String line) {
		this.addLineToQueue(line, AsyncLogger.RECURSION_LIMIT);
	}
	
	private void addLineToQueue(String line, int limit) {
		if (limit == 0) {
			throw new LogTooLongException();
		}
		
		// // Check credentials only if logs are sent to LE directly.
		// Check that we have all parameters set and socket appender running.
		// If DataHub mode is used then credentials check is ignored.
		if (!this.started && this.checkCredentials()) {
			this.dbg("Starting Logentries asynchronous socket appender");
			this.appender.start();
			this.started = true;
		}
		
		this.dbg("Queueing " + line);
		
		// If individual string is too long add it to the queue recursively as sub-strings
		if (line.length() > AsyncLogger.LOG_LENGTH_LIMIT) {
			if (!this.queue.offer(line.substring(0, AsyncLogger.LOG_LENGTH_LIMIT))) {
				this.queue.poll();
				if (!this.queue.offer(line.substring(0, AsyncLogger.LOG_LENGTH_LIMIT))) {
					this.dbg(AsyncLogger.QUEUE_OVERFLOW);
				}
			}
			this.addLineToQueue(line.substring(AsyncLogger.LOG_LENGTH_LIMIT, line.length()), limit - 1);
			
		} else {
			// Try to append data to queue
			if (!this.queue.offer(line)) {
				this.queue.poll();
				if (!this.queue.offer(line)) {
					this.dbg(AsyncLogger.QUEUE_OVERFLOW);
				}
			}
		}
	}
	
	/**
	 * Closes all connections to Logentries.
	 */
	public void close() {
		this.appender.interrupt();
		this.started = false;
		this.dbg("Closing Logentries asynchronous socket appender");
	}
	
	/**
	 * Prints the message given. Used for internal debugging.
	 *
	 * @param msg message to display
	 */
	void dbg(String msg) {
		if (this.debug) {
			if (!msg.endsWith(AsyncLogger.LINE_SEP)) {
				System.err.println(AsyncLogger.LE + msg);
			} else {
				System.err.print(AsyncLogger.LE + msg);
			}
		}
	}
	
	
	/**
	 * Asynchronous over the socket appender.
	 *
	 * @author Viliam Holub
	 *
	 */
	class SocketAppender extends Thread {
		
		/** Random number generator for delays between reconnection attempts. */
		final Random random = new Random();
		/** Logentries Client for connecting to Logentries via HTTP or TCP. */
		LogentriesClient le_client;
		
		
		/**
		 * Initializes the socket appender.
		 */
		SocketAppender() {
			super("Logentries Socket appender");
			// Don't block shut down
			this.setDaemon(true);
		}
		
		/**
		 * Opens connection to Logentries.
		 *
		 * @throws IOException
		 */
		void openConnection() throws IOException {
			if (this.le_client == null) {
				this.le_client = new LogentriesClient();
			}
			
			this.le_client.connect();
		}
		
		/**
		 * Tries to opens connection to Logentries until it succeeds.
		 *
		 * @throws InterruptedException
		 */
		void reopenConnection() throws InterruptedException {
			// Close the previous connection
			this.closeConnection();
			
			// Try to open the connection until we get through
			int root_delay = AsyncLogger.MIN_DELAY;
			while (true) {
				try {
					this.openConnection();
					
					// Success, leave
					return;
				} catch (IOException e) {
					// Get information if in debug mode
					if (AsyncLogger.this.debug) {
						AsyncLogger.this.dbg("Unable to connect to Logentries");
						e.printStackTrace();
					}
				}
				
				// Wait between connection attempts
				root_delay *= 2;
				if (root_delay > AsyncLogger.MAX_DELAY) {
					root_delay = AsyncLogger.MAX_DELAY;
				}
				int wait_for = root_delay + this.random.nextInt(root_delay);
				AsyncLogger.this.dbg("Waiting for " + wait_for + "ms");
				Thread.sleep(wait_for);
			}
		}
		
		/**
		 * Closes the connection. Ignores errors.
		 */
		void closeConnection() {
			
			if (this.le_client != null) {
				this.le_client.close();
			}
			
		}
		
		/**
		 * Builds the prefix message for the StringBuilder.
		 */
		private void buildPrefixMessage(StringBuilder sb) {
			if (!AsyncLogger.this.logID.isEmpty()) {
				sb.append(AsyncLogger.this.logID).append(" "); // Append LogID and separator between logID and the rest part of the message.
			}
			
			if (AsyncLogger.this.logHostName) {
				if (AsyncLogger.this.hostName.isEmpty()) {
					AsyncLogger.this.dbg("Host name is not defined by user - trying to obtain it from the environment.");
					try {
						AsyncLogger.this.hostName = InetAddress.getLocalHost().getHostName();
						sb.append("HostName=").append(AsyncLogger.this.hostName).append(" ");
					} catch (UnknownHostException e) {
						// We cannot resolve local host name - so won't use it at all.
						AsyncLogger.this.dbg("Failed to get host name automatically; Host name will not be used in prefix.");
					}
				} else {
					if (!AsyncLogger.this.checkIfHostNameValid(AsyncLogger.this.hostName)) {
						// User-defined HostName is invalid - e.g. with prohibited characters,
						// so we'll not use it.
						AsyncLogger.this.dbg("There are some prohibited characters found in the host name defined in the config; Host name will not be used in prefix.");
					} else {
						sb.append("HostName=").append(AsyncLogger.this.hostName).append(" ");
					}
				}
			}
		}
		
		/**
		 * Initializes the connection and starts to log.
		 *
		 */
		@Override
		public void run() {
			try {
				// Open connection
				this.reopenConnection();
				
				String logMessagePrefix = "";
				StringBuilder sb = new StringBuilder(logMessagePrefix);
				
				this.buildPrefixMessage(sb);
				
				boolean logPrefixEmpty;
				if (!(logPrefixEmpty = sb.toString().isEmpty())) {
					logMessagePrefix = sb.toString();
				}
				
				// Use StringBuilder here because if use just overloaded
				// + operator it may give much more work for allocator and GC.
				StringBuilder finalDataBuilder = new StringBuilder("");
				
				// Send data in queue
				while (true) {
					// Take data from queue
					String data = AsyncLogger.this.queue.take();
					
					// Replace platform-independent carriage return with unicode line separator character to format multi-line events nicely
					// in Logentries UI
					data = data.replace(AsyncLogger.LINE_SEP, "\u2028");
					
					finalDataBuilder.setLength(0); // Clear the buffer to be re-used - it may be faster than re-allocating space for new
													// String instances.
					
					// append the token to the start of the message.
					finalDataBuilder.append(AsyncLogger.this.token);
					
					// If message prefix (LogID + HostName) is not empty
					// then add it to the message.
					if (!logPrefixEmpty) {
						finalDataBuilder.append(logMessagePrefix);
					}
					
					// Append the event data
					finalDataBuilder.append(data).append('\n');
					
					// Get bytes of final event
					byte[] finalLine = finalDataBuilder.toString().getBytes(AsyncLogger.UTF8);
					
					// Send data, reconnect if needed
					while (true) {
						try {
							this.le_client.write(finalLine, 0, finalLine.length);
						} catch (IOException e) {
							// Reopen the lost connection
							this.reopenConnection();
							continue;
						}
						break;
					}
				}
			} catch (InterruptedException e) {
				// We got interrupted, stop
				AsyncLogger.this.dbg("Asynchronous socket writer interrupted");
				AsyncLogger.this.dbg("Queue had " + AsyncLogger.this.queue.size() + " lines left in it");
			}
			
			this.closeConnection();
		}
	}
}
