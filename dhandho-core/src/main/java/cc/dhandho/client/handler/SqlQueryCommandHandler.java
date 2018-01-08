package cc.dhandho.client.handler;

import com.google.gson.JsonObject;

import cc.dhandho.client.CommandContext;
import cc.dhandho.rest.handler.SqlQueryJsonHandler;
import cc.dhandho.util.JsonUtil;

public class SqlQueryCommandHandler extends DhandhoCommandHandler {

	@Override
	public void execute(CommandContext cc) {
		String[] sqlA = cc.getCommandLine().getLine().getArgs();
		StringBuilder sb = new StringBuilder();
		for (String s : sqlA) {
			sb.append(s).append(" ");
		}

		JsonObject req = new JsonObject();
		req.addProperty("sql", sb.toString());
		JsonObject json = (JsonObject) cc.getServer().handle(SqlQueryJsonHandler.class.getName(), req);
		cc.getWriter().writeLn(JsonUtil.toString(json, true));
	}

}
