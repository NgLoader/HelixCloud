package de.ngloader.helixcloud.api.plugin;

import java.lang.annotation.Annotation;

import de.ngloader.helixcloud.api.plugin.annotation.CommandInfo;
import de.ngloader.helixcloud.api.plugin.annotation.ListenerInfo;
import de.ngloader.helixcloud.api.plugin.annotation.PluginInfo;

public enum PluginClassType {

	MAIN(PluginInfo.class),
	COMMAND(CommandInfo.class),
	LISTENER(ListenerInfo.class);

	private final Class<? extends Annotation> clazz;

	private PluginClassType(Class<? extends Annotation> clazz) {
		this.clazz = clazz;
	}

	public Class<? extends Annotation> getClassInstance() {
		return this.clazz;
	}
}