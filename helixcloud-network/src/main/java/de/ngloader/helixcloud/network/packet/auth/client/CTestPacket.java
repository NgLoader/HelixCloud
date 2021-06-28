package de.ngloader.helixcloud.network.packet.auth.client;

import de.ngloader.helixcloud.network.packet.IPacket;
import de.ngloader.helixcloud.network.packet.auth.AuthClientPacketHandler;
import de.ngloader.helixcloud.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class CTestPacket implements IPacket<AuthClientPacketHandler> {

	private String message;

	public CTestPacket() { }

	public CTestPacket(String message) {
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
	public void handle(AuthClientPacketHandler handler) {
		handler.handleTestPacket(this);
	}

	public String getMessage() {
		return this.message;
	}
}