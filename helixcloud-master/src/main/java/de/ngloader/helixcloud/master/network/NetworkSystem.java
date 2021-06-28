package de.ngloader.helixcloud.master.network;

import java.net.InetSocketAddress;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import de.ngloader.helixcloud.master.HelixMaster;
import de.ngloader.helixcloud.network.NetworkHandler;
import de.ngloader.helixcloud.network.NetworkManager;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class NetworkSystem extends NetworkHandler {

	private InetSocketAddress address;
	private SslContext sslContext;

	public NetworkSystem(HelixMaster keinMaster) {
		SelfSignedCertificate selfSignedCertificate;
		try {
			selfSignedCertificate = new SelfSignedCertificate();
			this.sslContext = SslContextBuilder.forServer(selfSignedCertificate.certificate(), selfSignedCertificate.privateKey()).build();
		} catch (CertificateException | SSLException e) {
			e.printStackTrace();
		}
	}

	public void start(InetSocketAddress address) {
		this.address = address;

		this.startServer(this.address, 0, this.sslContext, (pipeline) -> {
			NetworkManager networkManager = new NetworkManager();
			networkManager.setPacketHandler(new AuthPacketHandlerServer());

			NetworkSystem.this.networkManagers.add(networkManager);
			pipeline.addLast("packet_handler", networkManager);
			
		});
	}
}
