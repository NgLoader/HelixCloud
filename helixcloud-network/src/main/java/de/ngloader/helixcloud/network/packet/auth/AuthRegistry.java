package de.ngloader.helixcloud.network.packet.auth;

import de.ngloader.helixcloud.network.packet.EnumPacketDirection;
import de.ngloader.helixcloud.network.packet.auth.client.CTestPacket;
import de.ngloader.helixcloud.network.packet.auth.server.STestPacket;
import de.ngloader.helixcloud.network.packet.standart.StandartRegistry;

public class AuthRegistry extends StandartRegistry {

	public AuthRegistry() {
		this.registerPacket(EnumPacketDirection.CLIENT, CTestPacket.class);
		this.registerPacket(EnumPacketDirection.SERVER, STestPacket.class);
	}
}