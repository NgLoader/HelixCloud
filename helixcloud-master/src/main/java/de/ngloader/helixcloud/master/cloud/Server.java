package de.ngloader.helixcloud.master.cloud;

import java.util.UUID;

public class Server {

	private UUID uuid;

	public Server(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getId() {
		return this.uuid;
	}
}
