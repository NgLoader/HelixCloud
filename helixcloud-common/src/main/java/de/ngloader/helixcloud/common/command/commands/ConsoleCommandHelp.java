package de.ngloader.helixcloud.common.command.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ngloader.helixcloud.api.common.command.IConsoleCommand;
import de.ngloader.helixcloud.api.common.command.IConsoleCommandMeta;
import de.ngloader.helixcloud.common.command.ConsoleCommandHandler;
import de.ngloader.helixcloud.common.command.ConsoleCommandInfo;

@IConsoleCommandMeta(aliases = { "help", "h" }, description = "List of all commands", usage = "help [command]")
public class ConsoleCommandHelp implements IConsoleCommand {

	private static final Logger LOGGER = LogManager.getLogger(ConsoleCommandHelp.class.getSimpleName());

	private final ConsoleCommandHandler commandHandler;

	public ConsoleCommandHelp(ConsoleCommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	@Override
	public void execute(String[] args) {
		LOGGER.info("[] ===-___-=== { Commands } ===-___-=== []");
		LOGGER.info(" ");

		for (ConsoleCommandInfo commandInfo : this.commandHandler.getCommands()) {
			LOGGER.info(String.format(" - %s | %s", commandInfo.meta.aliases()[0], commandInfo.meta.usage()));
		}

		LOGGER.info(" ");
		LOGGER.info("[] ===-___-=== { Commands } ===-___-=== []");
	}
}
