package cc.dhandho.rest.command;

import org.apache.commons.cli.CommandLine;

import cc.dhandho.commons.commandline.CommandType;

public class CommandContext {
	private CommandType type;
	private CommandLine commandLine;

	public CommandLine getCommandLine() {
		return commandLine;
	}

	private CommandExecutor executor;
	private boolean consumed;

	public CommandContext(CommandType type2, CommandLine cl, CommandExecutor executor) {
		this.type = type2;
		this.commandLine = cl;
		this.executor = executor;
	}

	public String[] getArgs() {
		return commandLine.getArgs();
	}

	public CommandExecutor getConsole() {
		return this.executor;
	}

	public Usage getUsage() {

		return this.getConsole().getUsage();
	}

	public void consume() {
		//
		this.consumed = true;
	}

}
