package de.ngloader.helixcloud.api.plugin.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PluginInfo {

	public String name();

	public String version();

	public String description = "";

	public String[] authors() default "";

	/*
	 * Dependency is need for the plugin to load!
	 */
	public String[] hardDepend() default "";

	/*
	 * Dependency with is needed for full function
	 */
	public String[] softDepend() default "";
}