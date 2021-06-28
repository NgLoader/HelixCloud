package de.ngloader.helixcloud.network.packet.standart;

import de.ngloader.helixcloud.network.packet.IPacketHandler;
import de.ngloader.helixcloud.network.packet.standart.server.SPacketStandartKeepAlive;

public interface StandartServerPacketHandler extends IPacketHandler {

	public void handleKeepAlive(SPacketStandartKeepAlive packet);
}