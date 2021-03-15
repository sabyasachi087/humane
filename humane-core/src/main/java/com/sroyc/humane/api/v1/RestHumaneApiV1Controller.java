package com.sroyc.humane.api.v1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RestController;

/**
 * A marker annotation for adding prefix path for Rest APIs
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RestController
public @interface RestHumaneApiV1Controller {
	
	@AliasFor(annotation = RestController.class)
	String value() default "";

}
