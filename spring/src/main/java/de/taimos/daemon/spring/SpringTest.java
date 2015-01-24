package de.taimos.daemon.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public abstract class SpringTest {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private AbstractXmlApplicationContext context;
	
	
	public AbstractXmlApplicationContext getContext() {
		return this.context;
	}
	
	public void start() {
		try {
			this.doBeforeSpringStart();
		} catch (Exception e) {
			this.logger.error("Before spring failed", e);
			throw new RuntimeException(e);
		}
		
		try {
			this.context = this.createSpringContext();
			String[] profiles = System.getProperty(Configuration.PROFILES, Configuration.PROFILES_TEST).split(",");
			this.context.getEnvironment().setActiveProfiles(profiles);
			
			final PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
			configurer.setProperties(this.loadProperties());
			this.context.addBeanFactoryPostProcessor(configurer);
			
			this.context.setConfigLocation(this.getSpringResource());
			this.context.refresh();
		} catch (Exception e) {
			this.logger.error("Spring context failed", e);
			throw new RuntimeException(e);
		}
		
		try {
			this.doAfterSpringStart();
		} catch (Exception e) {
			this.logger.error("After spring failed", e);
			throw new RuntimeException(e);
		}
	}
	
	protected void doAfterSpringStart() {
		//
	}
	
	protected void doBeforeSpringStart() {
		//
	}
	
	protected void doAfterSpringStop() {
		//
	}
	
	protected void doBeforeSpringStop() {
		//
	}
	
	/**
	 * @return the created Spring context
	 */
	protected AbstractXmlApplicationContext createSpringContext() {
		return new ClassPathXmlApplicationContext();
	}
	
	/**
	 * @return the name of the Spring resource
	 */
	protected String getSpringResource() {
		return "spring-test/beans.xml";
	}
	
	public void stop() {
		try {
			this.doBeforeSpringStop();
		} catch (Exception e) {
			this.logger.error("Before spring stop failed", e);
			throw new RuntimeException(e);
		}
		try {
			this.context.stop();
			this.context.close();
		} catch (Exception e) {
			this.logger.error("spring stop failed", e);
			throw new RuntimeException(e);
		}
		try {
			this.doAfterSpringStop();
		} catch (Exception e) {
			this.logger.error("After spring stop failed", e);
			throw new RuntimeException(e);
		}
	}
	
	private Properties loadProperties() {
		Map<String, String> props = new HashMap<>();
		props.put("serviceName", this.getServiceName());
		this.fillProperties(props);
		if (!props.containsKey(Configuration.SERVICE_PACKAGE)) {
			props.put(Configuration.SERVICE_PACKAGE, this.getClass().getPackage().getName());
		}
		
		Properties properties = new Properties();
		
		for (Entry<String, String> entry : props.entrySet()) {
			this.logger.info(String.format("Setting property: '%s' with value '%s'", entry.getKey(), entry.getValue()));
			properties.setProperty(entry.getKey(), entry.getValue());
			System.setProperty(entry.getKey(), entry.getValue());
		}
		return properties;
	}
	
	protected abstract String getServiceName();
	
	protected abstract void fillProperties(Map<String, String> props);
	
}
