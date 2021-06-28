package de.ngloader.helixcloud.api.plugin;

import java.nio.file.Path;

import de.ngloader.helixcloud.api.plugin.error.InvalidPluginException;

public interface PluginLoader<T> {


	public PluginClassLoader<T> createClassLoader(Path file) throws InvalidPluginException;

	public Plugin<T> loadPlugin(PluginClassLoader<T> classLoader) throws InvalidPluginException;

	public void enablePlugin(Plugin<T> plugin);
	public void disablePlugin(Plugin<T> plugin);
}