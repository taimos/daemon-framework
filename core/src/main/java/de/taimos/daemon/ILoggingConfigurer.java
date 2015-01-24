package de.taimos.daemon;

public interface ILoggingConfigurer {
	
	void initializeLogging() throws Exception;
	
	void reconfigureLogging() throws Exception;
	
	void simpleLogging() throws Exception;
	
}
