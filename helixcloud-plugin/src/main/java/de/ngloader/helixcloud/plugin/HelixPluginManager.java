package de.ngloader.helixcloud.plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;

import de.ngloader.helixcloud.api.plugin.Plugin;
import de.ngloader.helixcloud.api.plugin.PluginClassLoader;
import de.ngloader.helixcloud.api.plugin.PluginClassType;
import de.ngloader.helixcloud.api.plugin.PluginLoader;
import de.ngloader.helixcloud.api.plugin.PluginManager;
import de.ngloader.helixcloud.api.plugin.annotation.PluginInfo;
import de.ngloader.helixcloud.api.plugin.error.InvalidPluginException;

public class HelixPluginManager<T> implements PluginManager<T> {

	private final T server;
	private final Logger logger;

	private final List<Plugin<T>> plugins = new ArrayList<>();

	private final Path pluginDirectory;
	private final PluginLoader<T> pluginLoader;

	public HelixPluginManager(T server, Logger logger, Path directory) {
		this.server = server;
		this.logger = logger;

		this.pluginDirectory = directory;
		this.pluginLoader = new HelixPluginLoader<T>(this);
	}	

	@Override
	public void loadPlugins() throws IOException {
		if (!Files.exists(this.pluginDirectory)) {
			Files.createDirectories(this.pluginDirectory);
		}

		Map<String, PluginClassLoader<T>> classLoaders = new HashMap<>();
		Files.walk(this.pluginDirectory, 1).forEach(path -> {
			try {
				for (Pattern pattern : HelixPluginLoader.FILE_FILTERS) {
					String name = path.getFileName().toString();
					if (pattern.matcher(name).find()) {
						PluginClassLoader<T> classLoader = this.pluginLoader.createClassLoader(path);
						PluginInfo pluginInfo = classLoader.getFirstClassByType(PluginClassType.MAIN, PluginInfo.class);
						if (classLoaders.containsKey(pluginInfo.name())) {
							throw new InvalidPluginException("Plugin \"" + name + "\" was already loaded!");
						}

						classLoaders.put(pluginInfo.name().toLowerCase(), classLoader);
					}
				}
			} catch (Exception e) {
				this.logger.warn("Error by loading plugin \"" + path.getFileName().toString() + "\"!", e);
			}
		});

		/*
		 * Example2 -> Example1
		 * Example1 -> Example2
		 */

		Map<String, Set<PluginClassLoader<T>>> sorted = new HashMap<>();
		Map<PluginClassLoader<T>, String> nameByclassLoader = new HashMap<>();

		for (Iterator<PluginClassLoader<T>> iterator = classLoaders.values().iterator(); iterator.hasNext();) {
			PluginClassLoader<T> classLoader = iterator.next();
			PluginInfo pluginInfo = classLoader.getFirstClassByType(PluginClassType.MAIN, PluginInfo.class);

			boolean error = false;
			for (String hardDepend : pluginInfo.hardDepend()) {
				if (hardDepend == null || hardDepend.isEmpty()) {
					continue;
				}

				if (!classLoaders.containsKey(hardDepend.toLowerCase())) {
					this.logger.error("Error by loading plugin \"" + pluginInfo.name() + "\" dependency \"" + hardDepend + "\" was not found!");
					error = true;
					break;
				}
				hardDepend = hardDepend.toLowerCase();

				Set<PluginClassLoader<T>> list = sorted.get(hardDepend);
				if (list == null) {
					list = new HashSet<>();
					sorted.put(hardDepend, list);
				}

				//TODO checking is loop

				list.add(classLoader);
				nameByclassLoader.put(classLoader, hardDepend);
			}

			if (error) {
				iterator.remove();
				continue;
			}

			
			System.out.println("FINISH " + pluginInfo.name());
		}

		System.out.println("PRINT");
		for (Entry<String, Set<PluginClassLoader<T>>> entry : sorted.entrySet()) {
			System.out.println("-> " + entry.getKey());
			entry.getValue().forEach(System.out::println);
		}
		System.out.println("PRINTED");
	}

	@Override
	public Plugin<T> loadPlugin(Path path) throws InvalidPluginException {
		for (Pattern pattern : HelixPluginLoader.FILE_FILTERS) {
			String name = path.getFileName().toString();
			if (pattern.matcher(name).find()) {
				return this.loadPlugin(this.pluginLoader.createClassLoader(path));
			}
		}
		return null;
	}

	private Plugin<T> loadPlugin(PluginClassLoader<T> classLoader) throws InvalidPluginException {
		Plugin<T> plugin = this.pluginLoader.loadPlugin(classLoader);
		this.plugins.add(plugin);
		return plugin;
	}

	@Override
	public void enablePlugin(Plugin<T> plugin) {
		if (!plugin.isEnabled()) {
			plugin.getPluginLoader().enablePlugin(plugin);
		}
	}

	@Override
	public void disablePlugins() {
		for (Iterator<Plugin<T>> iterator = plugins.iterator(); iterator.hasNext();) {
			this.disablePlugin(iterator.next());
		}
	}

	@Override
	public void disablePlugin(Plugin<T> plugin) {
		if (plugin.isEnabled()) {
			plugin.getPluginLoader().disablePlugin(plugin);
		}
	}

	public void clearPlugins() {
		synchronized (this) {
			this.disablePlugins();
			this.plugins.clear();
		}
	}

	@Override
	public List<Plugin<T>> getPlugins() {
		return Collections.unmodifiableList(this.plugins);
	}

	@Override
	public Logger getLogger() {
		return this.logger;
	}

	@Override
	public T getServer() {
		return this.server;
	}
}