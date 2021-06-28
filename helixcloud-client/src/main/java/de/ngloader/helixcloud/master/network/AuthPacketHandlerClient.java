package de.ngloader.helixcloud.master.network;

import de.ngloader.helixcloud.network.packet.auth.AuthClientPacketHandler;
import de.ngloader.helixcloud.network.packet.auth.client.CTestPacket;
import de.ngloader.helixcloud.network.packet.auth.server.STestPacket;

public class AuthPacketHandlerClient extends StandartPacketHandlerClient implements AuthClientPacketHandler {

	@Override
	public void handleTestPacket(CTestPacket packet) {
		System.out.println("Message: " + packet.getMessage());
	}

	@Override
	public void onConnected() {
		this.networkManager.sendPacket(new STestPacket("Moin von Client"));
	}
}