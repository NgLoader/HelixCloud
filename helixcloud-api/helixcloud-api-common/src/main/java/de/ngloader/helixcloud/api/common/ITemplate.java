package de.ngloader.helixcloud.api.common;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public interface ITemplate {

	public UUID getID();

	public String getName();

	public Path getDirectory();

	public List<UUID> getAllowedClients();

	public boolean isStatic();
}