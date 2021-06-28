package de.ngloader.helix.example;

import de.ngloader.helixcloud.api.master.HelixMasterAPI;
import de.ngloader.helixcloud.api.plugin.Plugin;
import de.ngloader.helixcloud.api.plugin.annotation.PluginInfo;

@PluginInfo(name = "Example2", version = "0.0.1", authors = "NgLoader", hardDepend = "Example1")
public class Example2 extends Plugin<HelixMasterAPI> {

	@Override
	public void onLoad() {
		System.out.println("onLoad! 2");
	}

	@Override
	public void onEnable() {
		System.out.println("onEnable! 2");
		System.out.println(Example.INSTANCE.name + " world");
	}

	@Override
	public void onDisable() {
		System.out.println("onDisable! 2");
	}
}