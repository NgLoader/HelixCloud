package de.ngloader.helixcloud.master;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ngloader.helixcloud.master.network.NetworkSystem;

public class HelixClient {

	private static final Logger LOGGER = LogManager.getLogger(HelixClient.class);

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
			new HelixClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final NetworkSystem networkSystem;

	public HelixClient() throws Exception {
		LOGGER.info("Starting client...");

		this.networkSystem = new NetworkSystem(this);
		this.networkSystem.connect(new InetSocketAddress("0.0.0.0", 1234));

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			LOGGER.info("Stopping client...");
			LogManager.shutdown();
		}));

		while(true);
	}

	public NetworkSystem getNetworkSystem() {
		return this.networkSystem;
	}
}
