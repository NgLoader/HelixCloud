package de.ngloader.helixcloud.network.packet;

import de.ngloader.helixcloud.network.NetworkManager;

public interface IPacketHandler {

	public void setNetworkManager(NetworkManager manager);

	public default void onConnected() { };
	public default void onDisconnect(String reason) { };
}