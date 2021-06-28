package de.ngloader.helixcloud.network.packet.auth;

import de.ngloader.helixcloud.network.packet.IPacketHandler;
import de.ngloader.helixcloud.network.packet.auth.server.STestPacket;

public interface AuthServerPacketHandler extends IPacketHandler {

	public void handleTestPacket(STestPacket packet);
}