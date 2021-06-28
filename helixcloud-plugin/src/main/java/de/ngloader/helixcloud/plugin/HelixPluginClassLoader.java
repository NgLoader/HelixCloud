package de.ngloader.helixcloud.plugin;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import de.ngloader.helixcloud.api.plugin.Plugin;
import de.ngloader.helixcloud.api.plugin.PluginClassLoader;
import de.ngloader.helixcloud.api.plugin.PluginClassType;
import de.ngloader.helixcloud.api.plugin.error.InvalidPluginException;

public class HelixPluginClassLoader<T> extends PluginClassLoader<T> {

	private final Map<String, Class<?>> classes = new ConcurrentHashMap<>();

	private final Map<PluginClassType, Set<Class<?>>> classTypes = new ConcurrentHashMap<>();

	private final HelixPluginLoader<T> loader;
	private final Path file;

	private final JarFile jar;

	Plugin<T> plugin;

	static {
		ClassLoader.registerAsParallelCapable();
	}

	public HelixPluginClassLoader(HelixPluginLoader<T> loader, ClassLoader parent, Path file) throws IOException, InvalidPluginException {
		super(new URL[] { file.toUri().toURL() }, parent);
		this.loader = loader;
		this.file = file;
		this.jar = new JarFile(this.file.toFile());

		Enumeration<? extends ZipEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			String name = entry.getName();
			if (name.endsWith(".class")) {
				name = name.substring(0, name.length() - 6).replace("/", ".");

				try {
					Class<?> clazz = Class.forName(name, false, this);
					for (PluginClassType type : PluginClassType.values()) {
						Annotation annotation = clazz.getDeclaredAnnotation(type.getClassInstance());
						if (annotation != null) {
							Set<Class<?>> list = this.classTypes.get(type);
							if (list == null) {
								list = new HashSet<>();
								this.classTypes.put(type, list);
							}
							list.add(clazz);
						}
					}
				} catch (ClassNotFoundException e) {
					throw new InvalidPluginException("Unable to load plugin \"" + file.toString() + "\"", e);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createInstance() throws InvalidPluginException {
		try {
			Set<Class<?>> classes = this.classTypes.get(PluginClassType.MAIN);
			if (classes == null || classes.size() != 1) {
				throw new InvalidPluginException("Plugin has zero or multiply main class annotations!");
			}
			for (Class<?> clazz : classes) {
				Class<?> clazzFinal;
				try {
					clazzFinal = (Class<? extends Plugin<T>>) clazz.asSubclass(Plugin.class);
				} catch (ClassCastException e) {
					throw new InvalidPluginException("Main class is not extends of HelixPlugin", e);
				}
				this.plugin = (Plugin<T>) clazzFinal.getConstructor().newInstance();
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new InvalidPluginException("Plugin has zero or multiply main class annotations!", e);
		}
	}

	@Override
	public URL getResource(String name) {
		return this.findResource(name);
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		return this.findResources(name);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return this.findClass(name, true);
	}

	@Override
	public Class<?> findClass(String name, boolean globalSearch) throws ClassNotFoundException {
		Class<?> result = this.classes.get(name);
		if (result == null) {
			if (globalSearch) {
				result = this.loader.getClassByName(name);
			}

			if (result == null) {
				result = super.findClass(name);
			}

			if (result != null) {
				this.loader.setClass(name, result);
			}

			this.classes.put(name, result);
		}

		return result;
	}

	@Override
	public <A extends Annotation> A getFirstClassByType(PluginClassType type, Class<A> clazz) {
		for (Class<?> instance : this.classTypes.get(type)) {
			return instance.getDeclaredAnnotation(clazz);
		}
		return null;
	}

	@Override
	public <A extends Annotation> Set<A> getClassByType(PluginClassType type, Class<A> clazz) {
		Set<A> list = new HashSet<>();
		for (Class<?> instance : this.classTypes.get(type)) {
			list.add(instance.getDeclaredAnnotation(clazz));
		}
		return list;
	}

	@Override
	public Set<Class<?>> getClassByType(PluginClassType type) {
		return this.classTypes.get(type);
	}

	@Override
	public Map<PluginClassType, Set<Class<?>>> getClassTypes() {
		return this.classTypes;
	}

	@Override
	public Set<String> getClasses() {
		return this.classes.keySet();
	}

	@Override
	public Plugin<T> getPlugin() {
		return this.plugin;
	}

	@Override
	public Path getFile() {
		return this.file;
	}

	@Override
	public void close() throws IOException {
		try {
			super.close();
		} finally {
			this.jar.close();
		}
	}
}