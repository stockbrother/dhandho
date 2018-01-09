package cc.dhandho.client.handler.rest;

import com.google.gson.JsonObject;

import cc.dhandho.client.CommandContext;
import cc.dhandho.client.RestRequestCommandHandler;
import cc.dhandho.client.handler.MyCorpDispatcherCommandHandler;
import cc.dhandho.rest.handler.MyCorpJsonHandler;

public class MyCorpAddCommandHandler extends RestRequestCommandHandler {
	@Override
	protected String buildRequest(CommandContext cc, JsonObject req) {

		String corpId = cc.getCommandLine().getOptionValue(MyCorpDispatcherCommandHandler.OPT_a);

		req.addProperty("command", "add");
		req.addProperty("corpId", corpId);

		return MyCorpJsonHandler.class.getName();
	}

}
