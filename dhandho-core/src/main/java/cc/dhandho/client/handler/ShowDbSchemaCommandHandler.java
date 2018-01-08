package cc.dhandho.client.handler;

import com.google.gson.JsonObject;

import cc.dhandho.client.CommandContext;
import cc.dhandho.rest.handler.ShowDbSchemaJsonHandler;
import cc.dhandho.util.JsonUtil;

public class ShowDbSchemaCommandHandler extends DhandhoCommandHandler {

	@Override
	public void execute(CommandContext cc) {

		JsonObject req = new JsonObject();
		JsonObject json = (JsonObject) cc.getServer().handle(ShowDbSchemaJsonHandler.class.getName(), req);

		cc.getWriter().writeLn(JsonUtil.toString(json, true));
	}

}
