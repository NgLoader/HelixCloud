package de.ngloader.helixcloud.api.common;

import java.nio.file.Path;

public interface IModule {

	public abstract void onLoad();
	public abstract void onEnable();
	public abstract void onDisable();

	public String getName();
	public String getDescription();

	public String getVersion();

	public Path getDataFolder();
}