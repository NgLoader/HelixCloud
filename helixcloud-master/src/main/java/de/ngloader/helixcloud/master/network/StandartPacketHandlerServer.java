package de.ngloader.helixcloud.master.network;

import de.ngloader.helixcloud.network.NetworkManager;
import de.ngloader.helixcloud.network.packet.standart.StandartServerPacketHandler;
import de.ngloader.helixcloud.network.packet.standart.client.CPacketStandartDisconnect;
import de.ngloader.helixcloud.network.packet.standart.client.CPacketStandartKeepAlive;
import de.ngloader.helixcloud.network.packet.standart.server.SPacketStandartKeepAlive;

public class StandartPacketHandlerServer implements StandartServerPacketHandler, Runnable {

	protected NetworkManager networkManager;

	private boolean waitingKeepAlivePacket = false;
	private long keepAliveTimestamp;
	private int ping;

	@Override
	public void setNetworkManager(NetworkManager manager) {
		this.networkManager = manager;
	}

	@Override
	public void handleKeepAlive(SPacketStandartKeepAlive packet) {
		if (this.waitingKeepAlivePacket && this.keepAliveTimestamp == packet.getTime()) {
			this.ping = (int) (System.currentTimeMillis() - this.keepAliveTimestamp);
			this.waitingKeepAlivePacket = false;
			System.out.println("Ping: " + ping);
		} else {
			this.disconnect("Timeout");
		}
	}

	@Override
	public void run() {
		long timestamp = System.currentTimeMillis();
		if (timestamp - this.keepAliveTimestamp > 15000) {
			if (this.waitingKeepAlivePacket) {
				this.disconnect("Timeout");
			} else {
				this.waitingKeepAlivePacket = true;
				this.keepAliveTimestamp = timestamp;
				this.networkManager.sendPacket(new CPacketStandartKeepAlive(this.keepAliveTimestamp));
			}
		}
	}

	public void disconnect(String reason) {
		this.networkManager.sendPacket(new CPacketStandartDisconnect(reason), finish -> this.networkManager.close(reason));
		this.networkManager.stopReading();
	}

	public int getPing() {
		return this.ping;
	}
}