package cc.dhandho.rest.command.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.dhandho.commons.commandline.CommandType;
import cc.dhandho.rest.command.CommandContext;
import cc.dhandho.rest.command.CommandHandler;

public class HelpCommandHandler implements CommandHandler {

	@Override
	public JsonElement execute(CommandContext cc) {
		JsonObject rt = new JsonObject();
		StringBuilder sb = new StringBuilder();
		String[] as = cc.getArgs();
		if (as.length == 1) {
			// print help for specific command.
			String cname = as[0];
			CommandType cmd = cc.getConsole().getCommand(cname);
			if (cmd == null) {
				cc.getUsage().usageOfAll(cc, sb);

			} else {
				cc.getUsage().usageOfCommand(cmd, sb);
			}
		} else {
			// print all command list.
			cc.getUsage().usageOfAll(cc, sb);
		}

		rt.addProperty("message", sb.toString());
		cc.consume();

		return rt;
	}

}
