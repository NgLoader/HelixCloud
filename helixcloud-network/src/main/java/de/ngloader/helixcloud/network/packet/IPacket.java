package de.ngloader.helixcloud.network.packet;

import io.netty.buffer.ByteBuf;

public interface IPacket<T extends IPacketHandler> {

	public void read(ByteBuf buffer);
	public void write(ByteBuf buffer);

	public void handle(T handler);
}