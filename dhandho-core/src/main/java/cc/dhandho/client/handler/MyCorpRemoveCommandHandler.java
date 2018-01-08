package cc.dhandho.client.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.dhandho.client.CommandContext;
import cc.dhandho.rest.handler.MyCorpJsonHandler;
import cc.dhandho.util.JsonUtil;

public class MyCorpRemoveCommandHandler extends DhandhoCommandHandler {

	@Override
	public void execute(CommandContext cc) {
		String corpId = cc.getCommandLine().getOptionValue(MyCorpDispatcherCommandHandler.OPT_r);
		JsonObject req = new JsonObject();
		req.addProperty("command", "remove");
		req.addProperty("corpId", corpId);
		JsonElement res = cc.getServer().handle(MyCorpJsonHandler.class.getName(), req);
		cc.getWriter().writeLn(JsonUtil.toString(res, true));
		cc.consume();
	}

}
