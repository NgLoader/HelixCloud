package de.ngloader.helixcloud.common.command.commands;

import de.ngloader.helixcloud.api.common.command.IConsoleCommand;
import de.ngloader.helixcloud.api.common.command.IConsoleCommandMeta;

@IConsoleCommandMeta(aliases = { "exit", "shutdown" }, description = "Shutdown the cloud", usage = "exit")
public class ConsoleCommandExit implements IConsoleCommand {

	@Override
	public void execute(String[] args) {
		System.exit(0);
	}
}
