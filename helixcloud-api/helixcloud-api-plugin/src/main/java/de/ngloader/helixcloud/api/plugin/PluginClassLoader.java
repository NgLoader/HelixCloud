package de.ngloader.helixcloud.api.plugin;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import de.ngloader.helixcloud.api.plugin.error.InvalidPluginException;

public abstract class PluginClassLoader<T> extends URLClassLoader {

	public PluginClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public abstract void createInstance() throws InvalidPluginException;

	public abstract Class<?> findClass(String name, boolean globalSearch) throws ClassNotFoundException;

	public abstract <A extends Annotation> A getFirstClassByType(PluginClassType type, Class<A> clazz);

	public abstract <A extends Annotation> Set<A> getClassByType(PluginClassType type, Class<A> clazz);

	public abstract Set<Class<?>> getClassByType(PluginClassType type);

	public abstract Map<PluginClassType, Set<Class<?>>> getClassTypes();

	public abstract Set<String> getClasses();

	public abstract Plugin<T> getPlugin();

	public abstract Path getFile();
}