package de.ngloader.helixcloud.master.network;

import java.net.InetSocketAddress;

import javax.net.ssl.SSLException;

import de.ngloader.helixcloud.master.HelixClient;
import de.ngloader.helixcloud.network.NetworkHandler;
import de.ngloader.helixcloud.network.NetworkManager;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class NetworkSystem extends NetworkHandler {

	private InetSocketAddress address;
	private SslContext sslContext;

	public NetworkSystem(HelixClient keinClient) {
		try {
			this.sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		} catch (SSLException e) {
			e.printStackTrace();
		}
	}

	public void connect(InetSocketAddress address) {
		this.address = address;

		this.startClient(this.address, this.sslContext, (pipeline) -> {
			NetworkManager networkManager = new NetworkManager();
			networkManager.setPacketHandler(new AuthPacketHandlerClient());

			NetworkSystem.this.networkManagers.add(networkManager);
			pipeline.addLast("packet_handler", networkManager);
		});
	}
}
