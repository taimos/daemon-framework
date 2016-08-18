/*
 * Copyright (c) 2016. Taimos GmbH http://www.taimos.de
 */

package de.taimos.daemon.spring.conditional;

import java.util.Map;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ClassAvailableCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(ClassAvailable.class.getCanonicalName());
        if (attributes != null ) {
            Object value = attributes.get("value");
            if (value != null && value instanceof String) {
                try {
                    Class.forName((String) value);
                    return true;
                } catch (ClassNotFoundException e) {
                    return false;
                }
            }
        }
        return false;
    }

}
