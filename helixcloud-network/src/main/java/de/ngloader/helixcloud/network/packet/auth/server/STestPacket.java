package de.ngloader.helixcloud.network.packet.auth.server;

import de.ngloader.helixcloud.network.packet.IPacket;
import de.ngloader.helixcloud.network.packet.auth.AuthServerPacketHandler;
import de.ngloader.helixcloud.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class STestPacket implements IPacket<AuthServerPacketHandler> {

	private String message;

	public STestPacket() { }

	public STestPacket(String message) {
		this.message = message;
	}

	@Override
	public void read(ByteBuf buffer) {
		this.message = ByteBufUtil.readString(buffer);
	}

	@Override
	public void write(ByteBuf buffer) {
		ByteBufUtil.writeString(buffer, this.message);
	}

	@Override
	public void handle(AuthServerPacketHandler handler) {
		handler.handleTestPacket(this);
	}

	public String getMessage() {
		return this.message;
	}
}