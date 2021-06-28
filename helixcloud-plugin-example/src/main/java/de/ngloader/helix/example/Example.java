package de.ngloader.helix.example;

import de.ngloader.helixcloud.api.master.HelixMasterAPI;
import de.ngloader.helixcloud.api.plugin.Plugin;
import de.ngloader.helixcloud.api.plugin.annotation.PluginInfo;

@PluginInfo(name = "Example1", version = "0.0.1", authors = "NgLoader", hardDepend = "Example2")
public class Example extends Plugin<HelixMasterAPI> {

	public static Example INSTANCE;

	public String name = "Hello";

	public Example() {
		INSTANCE = this;
	}

	@Override
	public void onLoad() {
		System.out.println("onLoad! 1");
	}

	@Override
	public void onEnable() {
		System.out.println("onEnable! 1");
	}

	@Override
	public void onDisable() {
		System.out.println("onDisable! 1");
	}
}