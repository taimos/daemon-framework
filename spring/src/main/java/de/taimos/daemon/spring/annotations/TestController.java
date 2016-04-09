/*
 * Copyright (c) 2016. Taimos GmbH http://www.taimos.de
 */

package de.taimos.daemon.spring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import de.taimos.daemon.spring.Configuration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Controller
@Profile(Configuration.PROFILES_TEST)
public @interface TestController {
	
	/**
	 * The value may indicate a suggestion for a logical component name, to be turned into a Spring bean in case of an autodetected
	 * component.
	 * 
	 * @return the suggested component name, if any
	 */
	String value() default "";
}
