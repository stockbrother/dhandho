package cc.dhandho.client.handler;

import cc.dhandho.client.CommandContext;
import cc.dhandho.rest.handler.InputDataLoadJsonHandler;

public class InputDataLoadCommandHandler extends DhandhoCommandHandler {

	@Override
	public void execute(CommandContext cc) {

		cc.getServer().handle(InputDataLoadJsonHandler.class.getName());
		
	}

}
