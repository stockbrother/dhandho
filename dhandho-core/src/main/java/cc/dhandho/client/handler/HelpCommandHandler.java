package cc.dhandho.client.handler;

import cc.dhandho.client.CommandContext;
import cc.dhandho.commons.commandline.CommandType;

public class HelpCommandHandler implements CommandHandler {

	@Override
	public void execute(CommandContext cc) {

		String[] as = cc.getArgs();
		if (as.length == 1) {
			// print help for specific command.
			String cname = as[0];
			CommandType cmd = cc.getConsole().getCommand(cname);
			if (cmd == null) {
				cc.getUsage().usageOfAll(cc);
			} else {
				cc.getUsage().usageOfCommand(cc, cmd);
			}
		} else {
			// print all command list.
			cc.getUsage().usageOfAll(cc);
		}
		cc.consume();
	}

}
