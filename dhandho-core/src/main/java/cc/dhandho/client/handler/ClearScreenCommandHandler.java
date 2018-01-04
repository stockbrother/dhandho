package cc.dhandho.client.handler;

import cc.dhandho.client.CommandContext;

public class ClearScreenCommandHandler implements CommandHandler {

	@Override
	public void execute(CommandContext cc) {
		
		cc.getConsole().clear();
		
	}

}
