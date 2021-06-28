package de.ngloader.helixcloud.master.network;

import de.ngloader.helixcloud.network.NetworkManager;
import de.ngloader.helixcloud.network.packet.standart.StandartClientPacketHandler;
import de.ngloader.helixcloud.network.packet.standart.client.CPacketStandartDisconnect;
import de.ngloader.helixcloud.network.packet.standart.client.CPacketStandartKeepAlive;
import de.ngloader.helixcloud.network.packet.standart.server.SPacketStandartKeepAlive;

public class StandartPacketHandlerClient implements StandartClientPacketHandler {

	protected NetworkManager networkManager;

	@Override
	public void setNetworkManager(NetworkManager manager) {
		this.networkManager = manager;
	}

	@Override
	public void handleKeepAlive(CPacketStandartKeepAlive packet) {
		this.networkManager.sendPacket(new SPacketStandartKeepAlive(packet.getTime()));
	}

	@Override
	public void handleDisconnect(CPacketStandartDisconnect packet) {
		this.networkManager.close(packet.getReason());
	}

	@Override
	public void onDisconnect(String reason) {
		System.out.println(String.format("Disconnected. Reason: %s", reason));
		//TODO log disconnected
	}
}