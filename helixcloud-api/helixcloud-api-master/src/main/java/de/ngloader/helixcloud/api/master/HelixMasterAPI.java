package de.ngloader.helixcloud.api.master;

import de.ngloader.helixcloud.api.plugin.PluginManager;

public interface HelixMasterAPI {

	public PluginManager<HelixMasterAPI> getPluginManager();
}