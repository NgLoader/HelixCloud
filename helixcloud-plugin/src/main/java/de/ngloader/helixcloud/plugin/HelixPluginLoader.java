package de.ngloader.helixcloud.plugin;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ngloader.helixcloud.api.plugin.Plugin;
import de.ngloader.helixcloud.api.plugin.PluginClassLoader;
import de.ngloader.helixcloud.api.plugin.PluginClassType;
import de.ngloader.helixcloud.api.plugin.PluginLoader;
import de.ngloader.helixcloud.api.plugin.PluginManager;
import de.ngloader.helixcloud.api.plugin.annotation.PluginInfo;
import de.ngloader.helixcloud.api.plugin.error.InvalidPluginException;

public class HelixPluginLoader<T> implements PluginLoader<T> {

	public static final Pattern[] FILE_FILTERS = new Pattern[] { Pattern.compile(".+\\.jar$") };

	private final Map<String, Class<?>> classes = new ConcurrentHashMap<>();
	private final List<HelixPluginClassLoader<T>> loaders = new CopyOnWriteArrayList<>();

	final PluginManager<T> pluginManager;

	public HelixPluginLoader(PluginManager<T> pluginManager) {
		this.pluginManager = pluginManager;
	}

	@Override
	public HelixPluginClassLoader<T> createClassLoader(Path file) throws InvalidPluginException {
		if (!Files.exists(file)) {
			throw new InvalidPluginException(new FileNotFoundException(file.toString() + " does not exist"));
		}

		HelixPluginClassLoader<T> loader;
		try {
			loader = new HelixPluginClassLoader<T>(this, this.getClass().getClassLoader(), file);
		} catch (InvalidPluginException e) {
			throw e;
		} catch (Throwable e) {
			throw new InvalidPluginException(e);
		}

		this.loaders.add(loader);
		return loader;
	}

	@Override
	public Plugin<T> loadPlugin(PluginClassLoader<T> loader) throws InvalidPluginException {
		try {
			loader.createInstance();
			loader.getPlugin().initialize(this.pluginManager.getServer(), loader, this, this.pluginManager, loader.getFile());
		} catch (InvalidPluginException e) {
			throw e;
		} catch (Throwable e) {
			throw new InvalidPluginException(e);
		}

		return loader.getPlugin();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void enablePlugin(Plugin<T> plugin) {
		if (!plugin.isEnabled()) {
			Logger logger = LogManager.getLogger(plugin.getClass());
			PluginInfo pluginInfo = plugin.getClassLoader().getFirstClassByType(PluginClassType.MAIN, PluginInfo.class);
			logger.info("Enabling " + pluginInfo.name());

			Plugin<T> helixPlugin = (Plugin<T>) plugin;
			ClassLoader pluginClassLoader = helixPlugin.getClassLoader();
			if (!this.loaders.contains(pluginClassLoader)) {
				this.loaders.add((HelixPluginClassLoader<T>) pluginClassLoader);
			}

			try {
				helixPlugin.setEnabled(true);
			} catch (Exception e) {
				logger.warn("Error by enabling plugin '" + pluginInfo.name() + "'", e);
			}
		}
	}

	@Override
	public void disablePlugin(Plugin<T> plugin) {
		if (plugin.isEnabled()) {
			Logger logger = LogManager.getLogger(plugin.getClass());
			PluginInfo pluginInfo = plugin.getClassLoader().getFirstClassByType(PluginClassType.MAIN, PluginInfo.class);
			logger.info("Disabling " + pluginInfo.name());

			Plugin<T> helixPlugin = (Plugin<T>) plugin;
			PluginClassLoader<T> classLoader = helixPlugin.getClassLoader();

			try {
				helixPlugin.setEnabled(false);
			} catch (Exception e) {
				logger.warn("Error by disabling plugin '" + pluginInfo.name() + "'", e);
			} finally {
				this.loaders.remove(classLoader);

				Set<String> classes = classLoader.getClasses();
				for (Iterator<String> iterator = classes.iterator(); iterator.hasNext();) {
					this.removeClass(iterator.next());
				}
			}
		}
	}

	public void setClass(String name, Class<?> result) {
		this.classes.put(name, result);
	}

	public void removeClass(String name) {
		this.classes.remove(name);
	}

	Class<?> getClassByName(String name) {
		Class<?> cachedClass = this.classes.get(name);
		if (cachedClass != null) {
			return cachedClass;
		}

		for (Iterator<HelixPluginClassLoader<T>> iterator = this.loaders.iterator(); iterator.hasNext();) {
			HelixPluginClassLoader<T> loader = iterator.next();
			try {
				 cachedClass = loader.findClass(name, false);
			} catch (ClassNotFoundException e) { }

			if (cachedClass != null) {
				return cachedClass;
			}
		}

		return null;
	}
}