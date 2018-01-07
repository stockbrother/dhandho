package cc.dhandho.client.handler;

import cc.dhandho.client.CommandContext;

public class ExitCommandHandler implements CommandHandler {

	@Override
	public void execute(CommandContext cc) {
		
		cc.getConsole().shutdownAsync();
		cc.consume();
	}

}
