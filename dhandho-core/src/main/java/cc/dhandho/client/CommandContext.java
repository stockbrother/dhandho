package cc.dhandho.client;

import cc.dhandho.commons.commandline.CommandAndLine;
import cc.dhandho.commons.commandline.CommandLineWriter;
import cc.dhandho.rest.server.DhoServer;

public class CommandContext {
	private CommandAndLine commandLine;

	/**
	 * If a command line not consumed, framework will(as the default behavior) print
	 * usage information.
	 */
	private boolean consumed;

	public boolean isConsumed() {
		return consumed;
	}

	public CommandContext consume() {
		this.consumed = true;
		return this;
	}

	public CommandContext(CommandAndLine cl) {
		this.commandLine = cl;
	}

	public CommandAndLine getCommandLine() {
		return commandLine;
	}

	public DhandhoCliConsole getConsole() {
		return (DhandhoCliConsole) this.commandLine.getConsole();
	}

	public CommandLineWriter getWriter() {
		return this.getConsole().peekWriter();
	}

	public String[] getArgs() {
		return commandLine.getArgs();
	}

	public DhoServer getServer() {
		return this.getConsole().getServer();
	}

	/**
	 * Show usage of the command.
	 */
	public void usage() {

	}

}
