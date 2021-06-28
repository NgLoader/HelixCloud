package de.ngloader.helixcloud.network.packet.auth;

import de.ngloader.helixcloud.network.packet.IPacketHandler;
import de.ngloader.helixcloud.network.packet.auth.client.CTestPacket;

public interface AuthClientPacketHandler extends IPacketHandler {

	public void handleTestPacket(CTestPacket packet);
}