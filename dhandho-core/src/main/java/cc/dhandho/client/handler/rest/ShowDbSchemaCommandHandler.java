package cc.dhandho.client.handler.rest;

import com.google.gson.JsonObject;

import cc.dhandho.client.CommandContext;
import cc.dhandho.client.RestRequestCommandHandler;
import cc.dhandho.rest.handler.ShowDbSchemaJsonHandler;

public class ShowDbSchemaCommandHandler extends RestRequestCommandHandler {

	@Override
	protected String buildRequest(CommandContext cc, JsonObject req) {

		return ShowDbSchemaJsonHandler.class.getName();
	}

}
