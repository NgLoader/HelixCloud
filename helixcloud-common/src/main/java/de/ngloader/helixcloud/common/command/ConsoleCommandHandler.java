package de.ngloader.helixcloud.common.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ngloader.helixcloud.api.common.command.IConsoleCommand;
import de.ngloader.helixcloud.api.common.command.IConsoleCommandMeta;
import de.ngloader.helixcloud.common.command.commands.ConsoleCommandExit;
import de.ngloader.helixcloud.common.command.commands.ConsoleCommandHelp;

public class ConsoleCommandHandler {

	private static final Logger LOGGER = LogManager.getLogger(ConsoleCommandHandler.class.getSimpleName());

	private final Map<String, ConsoleCommandInfo> commands = new ConcurrentHashMap<>();
	private final List<ConsoleCommandInfo> commandInfos = new LinkedList<>();
	private final Queue<String> queue = new ConcurrentLinkedQueue<>();

	private boolean running = true;

	public ConsoleCommandHandler() {
		this.registerCommand(new ConsoleCommandExit());
		this.registerCommand(new ConsoleCommandHelp(this));

		Thread consoleReaderThread = new Thread(this::readConsole, "commandhandler-reader");
		Thread consoleExecuterThread = new Thread(this::handleInput, "commandhandler-executer");

		consoleReaderThread.setDaemon(true);
		consoleExecuterThread.setDaemon(true);

		consoleReaderThread.start();
		consoleExecuterThread.start();
	}

	public void readConsole() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			String input;
			while ((input = reader.readLine()) != null && this.running) {
				this.queue.add(input);
			}
		} catch (IOException e) {
			throw new RuntimeException("Exception handling console input", e);
		}
	}

	public void handleInput() {
		while (this.running) {
			while (!this.queue.isEmpty()) {
				String input = this.queue.poll();
				String[] split = input.split("\\s+");
				String alias = split[0].toLowerCase();
				String[] args = Arrays.copyOfRange(split, 1, split.length);

				ConsoleCommandInfo commandInfo = this.commands.get(alias);
				if (commandInfo == null) {
					LOGGER.warn("Unknown command, please use 'help' for more information.");
					continue;
				}

				try {
					commandInfo.command.execute(args);
				} catch (Exception e) {
					LOGGER.fatal("An error has occured: ", e);
				}
			}
		}
	}

	public void registerCommand(IConsoleCommand command) {
		if (command == null) {
			LOGGER.warn("Unable to register null command object!");
			return;
		}

		IConsoleCommandMeta meta = command.getClass().getAnnotation(IConsoleCommandMeta.class);
		if (meta == null) {
			LOGGER.warn(String.format("Command '%s' has no meta class!", command.getClass().getSimpleName()));
		}

		ConsoleCommandInfo info = new ConsoleCommandInfo(command, meta);
		this.commandInfos.add(info);
		for (String alias : meta.aliases()) {
			this.commands.put(alias, info);
			LOGGER.debug(String.format("Registered command '%s' to '%s'", alias, command.getClass().getSimpleName()));
		}
	}

	public void shutdown() {
		this.running = false;
		this.queue.clear();
	}

	public Collection<ConsoleCommandInfo> getCommands() {
		return this.commandInfos;
	}
}
