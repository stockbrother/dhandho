package cc.dhandho.client.handler.rest;

import com.google.gson.JsonObject;

import cc.dhandho.client.CommandContext;
import cc.dhandho.client.RestRequestCommandHandler;
import cc.dhandho.rest.handler.SqlQueryJsonHandler;

public class SqlQueryCommandHandler extends RestRequestCommandHandler {

	@Override
	protected String buildRequest(CommandContext cc, JsonObject req) {

		String[] sqlA = cc.getCommandLine().getLine().getArgs();
		StringBuilder sb = new StringBuilder();
		for (String s : sqlA) {
			sb.append(s).append(" ");
		}

		req.addProperty("sql", sb.toString());
		return SqlQueryJsonHandler.class.getName();
	}

}
