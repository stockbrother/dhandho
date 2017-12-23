package cc.dhandho.client;

import cc.dhandho.commons.commandline.CommandAndLine;

public class HelpCommandHandler implements CommandHandler {

	@Override
	public void execute(CommandAndLine line) {
		line.getConsole().peekWriter().writeLine("this is help command.");
	}

}
