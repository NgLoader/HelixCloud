package de.ngloader.helixcloud.api.plugin;

import java.nio.file.Path;

public class Plugin <T> {

	private T server;

	private PluginClassLoader<T> classLoader;
	private PluginLoader<T> pluginLoader;

	private PluginManager<T> pluginManager;

	private Path file;

	private boolean enabled = false;

	public void initialize(T server, PluginClassLoader<T> classLoader, PluginLoader<T> pluginLoader,
			PluginManager<T> pluginManager, Path file) {
		this.server = server;
		this.classLoader = classLoader;
		this.pluginLoader = pluginLoader;
		this.pluginManager = pluginManager;
		this.file = file;
	}

	public void onLoad() { }
	public void onEnable() { }
	public void onDisable() { }

	public void setEnabled(boolean enabled) {
		if (this.enabled != enabled) {
			this.enabled = enabled;
			if (this.enabled) {
				this.onEnable();
			} else {
				this.onDisable();
			}
		}
	}

	public T getServer() {
		return this.server;
	}

	public PluginClassLoader<T> getClassLoader() {
		return this.classLoader;
	}

	public PluginLoader<T> getPluginLoader() {
		return this.pluginLoader;
	}

	public PluginManager<T> getPluginManager() {
		return this.pluginManager;
	}

	public Path getFile() {
		return this.file;
	}

	public boolean isEnabled() {
		return this.enabled;
	}
}