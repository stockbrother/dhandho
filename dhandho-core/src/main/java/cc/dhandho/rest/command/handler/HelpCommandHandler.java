package cc.dhandho.rest.command.handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.dhandho.commons.commandline.CommandType;
import cc.dhandho.rest.command.CommandContext;
import cc.dhandho.rest.command.CommandHandler;

public class HelpCommandHandler implements CommandHandler {

	@Override
	public JsonElement execute(CommandContext cc) {
		JsonObject rt = new JsonObject();
		rt.addProperty("type", "table");
		JsonArray cnames = new JsonArray();
		cnames.add("No.");
		cnames.add("Line.");
		rt.add("columnNames", cnames);

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
		String[] rows = sb.toString().split("\n");
		JsonArray rowArray = new JsonArray();
		for (int i = 0; i < rows.length; i++) {
			JsonArray row = new JsonArray();
			row.add(i);
			row.add(rows[i]);
			rowArray.add(row);
		}
		rt.add("rowArray", rowArray);

		cc.consume();

		return rt;
	}

}
