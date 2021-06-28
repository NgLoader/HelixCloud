package de.ngloader.helixcloud.network.packet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.ngloader.helixcloud.network.packet.auth.AuthRegistry;

public class PacketRegistry {

	public static final AuthRegistry AUTH = new AuthRegistry();

	private final Map<Class<? extends IPacket<? extends IPacketHandler>>, Integer> serverPacketByClass = new HashMap<>();
	private final Map<Integer, Class<? extends IPacket<? extends IPacketHandler>>> serverPacketById = new HashMap<>();

	private final Map<Class<? extends IPacket<? extends IPacketHandler>>, Integer> clientPacketByClass = new HashMap<>();
	private final Map<Integer, Class<? extends IPacket<? extends IPacketHandler>>> clientPacketById = new HashMap<>();

	private Map<Class<? extends IPacket<? extends IPacketHandler>>, Integer> getPacketByClass(EnumPacketDirection packetDirection) {
		return packetDirection == EnumPacketDirection.SERVER ? this.serverPacketByClass : this.clientPacketByClass;
	}

	private Map<Integer, Class<? extends IPacket<? extends IPacketHandler>>> getPacketById(EnumPacketDirection packetDirection) {
		return packetDirection == EnumPacketDirection.SERVER ? this.serverPacketById : this.clientPacketById;
	}

	protected void registerPacket(EnumPacketDirection packetDirection, Class<? extends IPacket<? extends IPacketHandler>> packet) {
		Map<Class<? extends IPacket<? extends IPacketHandler>>, Integer> byClass = this.getPacketByClass(packetDirection);
		Map<Integer, Class<? extends IPacket<? extends IPacketHandler>>> byId = this.getPacketById(packetDirection);

		int id = byClass.size();
		while (byClass.containsValue(id) || byId.containsKey(id)) {
			id++;
		}

		byClass.put(packet, id);
		byId.put(id, packet);
	}

	public int getId(EnumPacketDirection packetDirection, IPacket<?> packet) {
		return this.getPacketByClass(packetDirection).getOrDefault(packet.getClass(), -1);
	}

	public IPacket<?> getPacket(EnumPacketDirection packetDirection, int id) throws IOException, ReflectiveOperationException {
		Class<? extends IPacket<? extends IPacketHandler>> clazz = this.getPacketById(packetDirection).get(id);
		if (clazz == null) {
			throw new IOException("Packet id not found!");
		}
		return clazz.getConstructor().newInstance();
	}
}