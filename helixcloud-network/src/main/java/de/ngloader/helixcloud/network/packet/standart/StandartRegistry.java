package de.ngloader.helixcloud.network.packet.standart;

import de.ngloader.helixcloud.network.packet.EnumPacketDirection;
import de.ngloader.helixcloud.network.packet.PacketRegistry;
import de.ngloader.helixcloud.network.packet.standart.client.CPacketStandartDisconnect;
import de.ngloader.helixcloud.network.packet.standart.client.CPacketStandartKeepAlive;
import de.ngloader.helixcloud.network.packet.standart.server.SPacketStandartKeepAlive;

public class StandartRegistry extends PacketRegistry {

	public StandartRegistry() {
		this.registerPacket(EnumPacketDirection.CLIENT, CPacketStandartKeepAlive.class);
		this.registerPacket(EnumPacketDirection.CLIENT, CPacketStandartDisconnect.class);

		this.registerPacket(EnumPacketDirection.SERVER, SPacketStandartKeepAlive.class);
	}
}