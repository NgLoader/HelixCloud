package de.ngloader.helixcloud.network.packet.standart;

import de.ngloader.helixcloud.network.packet.IPacketHandler;
import de.ngloader.helixcloud.network.packet.standart.client.CPacketStandartDisconnect;
import de.ngloader.helixcloud.network.packet.standart.client.CPacketStandartKeepAlive;

public interface StandartClientPacketHandler extends IPacketHandler {

	public void handleKeepAlive(CPacketStandartKeepAlive packet);

	public void handleDisconnect(CPacketStandartDisconnect packet);
}