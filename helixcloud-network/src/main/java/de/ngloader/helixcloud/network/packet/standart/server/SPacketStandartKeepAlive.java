package de.ngloader.helixcloud.network.packet.standart.server;

import de.ngloader.helixcloud.network.packet.IPacket;
import de.ngloader.helixcloud.network.packet.standart.StandartServerPacketHandler;
import io.netty.buffer.ByteBuf;

public class SPacketStandartKeepAlive implements IPacket<StandartServerPacketHandler> {

	private long time;

	public SPacketStandartKeepAlive() { }

	public SPacketStandartKeepAlive(long time) {
		this.time = time;
	}

	@Override
	public void read(ByteBuf buffer) {
		this.time = buffer.readLong();
	}

	@Override
	public void write(ByteBuf buffer) {
		buffer.writeLong(this.time);
	}

	@Override
	public void handle(StandartServerPacketHandler handler) {
		handler.handleKeepAlive(this);
	}

	public long getTime() {
		return this.time;
	}
}