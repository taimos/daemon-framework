package com.logentries.net;

/**
 * Thrown when a log + timestamps etc. is longer than {@link com.logentries.net.AsyncLogger#LOG_LENGTH_LIMIT} chars.
 */
public class LogTooLongException extends RuntimeException {
	
	private static final long serialVersionUID = 8962340419595016427L;
	
}
