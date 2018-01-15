package cc.dhandho.command;

import org.apache.commons.cli.CommandLine;

import cc.dhandho.commons.commandline.CommandType;

public class CommandContext {
	private CommandType type;
	private CommandLine commandLine;
	private CommandExecutor executor;
	private boolean consumed;

	public CommandContext(CommandType type2, CommandLine cl) {
		this.type = type2;
		this.commandLine = cl;
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
