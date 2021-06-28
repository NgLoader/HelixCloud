package de.ngloader.helixcloud.api.common.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface IConsoleCommandMeta {

	String[] aliases();

	String description();

	String usage();
}
