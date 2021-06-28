package de.ngloader.helixcloud.common.command;

import de.ngloader.helixcloud.api.common.command.IConsoleCommand;
import de.ngloader.helixcloud.api.common.command.IConsoleCommandMeta;

public class ConsoleCommandInfo {

	public IConsoleCommand command;
	public IConsoleCommandMeta meta;

	public ConsoleCommandInfo(IConsoleCommand command, IConsoleCommandMeta meta) {
		this.command = command;
		this.meta = meta;
	}
}