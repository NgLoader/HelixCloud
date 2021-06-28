package de.ngloader.helixcloud.api.plugin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.Logger;

import de.ngloader.helixcloud.api.plugin.error.InvalidPluginException;

public interface PluginManager<T> {

	public Plugin<T> loadPlugin(Path path) throws InvalidPluginException;
	public void loadPlugins() throws IOException;

	public void enablePlugin(Plugin<T> plugin);
	public void disablePlugin(Plugin<T> plugin);

	public void disablePlugins();

	public List<Plugin<T>> getPlugins();

	public Logger getLogger();

	public T getServer();
}