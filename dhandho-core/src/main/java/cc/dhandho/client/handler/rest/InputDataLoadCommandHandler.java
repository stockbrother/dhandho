package cc.dhandho.client.handler.rest;

import com.google.gson.JsonObject;

import cc.dhandho.client.CommandContext;
import cc.dhandho.client.RestRequestCommandHandler;
import cc.dhandho.rest.handler.InputDataLoadJsonHandler;

public class InputDataLoadCommandHandler extends RestRequestCommandHandler {

	@Override
	protected String buildRequest(CommandContext cc, JsonObject req) {	
		return InputDataLoadJsonHandler.class.getName();
	}

}
