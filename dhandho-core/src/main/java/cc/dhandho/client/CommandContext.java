package cc.dhandho.client;

import cc.dhandho.commons.commandline.CommandAndLine;
import cc.dhandho.commons.commandline.CommandLineWriter;
import cc.dhandho.server.DhandhoServer;

public class CommandContext {
	private CommandAndLine commandLine;

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

	public DhandhoServer getServer() {
		return this.getConsole().getServer();
	}

}
