package cc.dhandho.client;

import cc.dhandho.commons.commandline.CommandAndLine;

public class ExitCommandHandler implements CommandHandler {

	@Override
	public void execute(CommandAndLine line) {
		
		line.getConsole().shutdownAsync();
		
	}

}
