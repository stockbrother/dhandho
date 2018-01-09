package cc.dhandho.client.handler.rest;

import com.google.gson.JsonObject;

import cc.dhandho.client.CommandContext;
import cc.dhandho.client.RestRequestCommandHandler;
import cc.dhandho.rest.handler.MyCorpJsonHandler;

public class MyCorpShowCommandHandler extends RestRequestCommandHandler {

	@Override
	protected String buildRequest(CommandContext cc, JsonObject req) {
		req.addProperty("command", "show");
		return MyCorpJsonHandler.class.getName();
	}

}
