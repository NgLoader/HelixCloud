package de.ngloader.helixcloud.api.common.server;

import java.util.UUID;

import de.ngloader.helixcloud.api.common.IProperties;
import de.ngloader.helixcloud.api.common.ITemplate;

public interface IServer {

	public UUID getID();

	public ITemplate getTemplate();

	public IProperties getProperties();
}