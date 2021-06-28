package de.ngloader.helixcloud.master.network;

import de.ngloader.helixcloud.network.packet.auth.AuthServerPacketHandler;
import de.ngloader.helixcloud.network.packet.auth.client.CTestPacket;
import de.ngloader.helixcloud.network.packet.auth.server.STestPacket;

public class AuthPacketHandlerServer extends StandartPacketHandlerServer implements AuthServerPacketHandler {

	@Override
	public void handleTestPacket(STestPacket packet) {
		System.out.println("Message: " + packet.getMessage());
		this.disconnect("jolo");
	}

	@Override
	public void onConnected() {
		this.networkManager.sendPacket(new CTestPacket("Moin von Server"));
	}

	@Override
	public void onDisconnect(String reason) {
		System.out.println("Disconnected: " + reason);
	}
}