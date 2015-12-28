package net.shadowfacts.activator.compat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shadowfacts
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Compat {

	/**
	 * @return Mod id of this compat module
	 */
	String value();

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface PreInit {

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface Init {

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface PostInit {

	}

}
