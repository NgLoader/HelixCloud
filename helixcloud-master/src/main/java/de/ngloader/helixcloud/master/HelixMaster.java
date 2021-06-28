package de.ngloader.helixcloud.master;

import java.net.InetSocketAddress;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ngloader.helixcloud.api.master.HelixMasterAPI;
import de.ngloader.helixcloud.api.plugin.PluginManager;
import de.ngloader.helixcloud.common.command.ConsoleCommandHandler;
import de.ngloader.helixcloud.master.network.NetworkSystem;
import de.ngloader.helixcloud.plugin.HelixPluginManager;

public class HelixMaster implements HelixMasterAPI {

	private static final Logger LOGGER = LogManager.getLogger(HelixMaster.class);

	public static void main(String[] args) {
		int threads = 32;
		for (int i = 0; i < args.length; i++) {
			switch (args[i].toLowerCase()) {
			case "--threads":
				threads = Integer.valueOf(args[i + 1]);
				break;

			case "--debug":
				//TODO
				break;

			default:
				break;
			}
		}

		System.setProperty("io.netty.eventLoopThreads", Integer.toString(threads));
 
		try {
			new HelixMaster();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final NetworkSystem networkSystem;
	private final ConsoleCommandHandler commandHandler;

	private final HelixPluginManager<HelixMasterAPI> pluginManager;

	public HelixMaster() throws Exception {
		this.networkSystem = new NetworkSystem(this);
		this.networkSystem.start(new InetSocketAddress("0.0.0.0", 1234));

		this.commandHandler = new ConsoleCommandHandler();

		this.pluginManager = new HelixPluginManager<HelixMasterAPI>(this, LogManager.getLogger(HelixPluginManager.class), Path.of("./plugins"));
		this.pluginManager.loadPlugins();
		this.pluginManager.getPlugins().forEach(plugin -> this.pluginManager.enablePlugin(plugin));

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			LOGGER.info("Stopping master...");

			this.commandHandler.shutdown();
			this.networkSystem.close();

			LOGGER.info("Godnight.");
			LogManager.shutdown();
		}));

		while (true);
	}

	public NetworkSystem getNetworkSystem() {
		return this.networkSystem;
	}

	public ConsoleCommandHandler getCommandHandler() {
		return this.commandHandler;
	}

	@Override
	public PluginManager<HelixMasterAPI> getPluginManager() {
		return this.pluginManager;
	}
}
