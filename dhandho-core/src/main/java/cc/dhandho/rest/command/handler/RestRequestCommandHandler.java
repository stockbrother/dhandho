package cc.dhandho.rest.command.handler;

import com.age5k.jcps.JcpsException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.dhandho.rest.command.CommandContext;
import cc.dhandho.rest.command.CommandHandler;

public abstract class RestRequestCommandHandler implements CommandHandler {

	public RestRequestCommandHandler() {

	}

	@Override
	public JsonElement execute(CommandContext cc) {
		JsonObject req = new JsonObject();
		String handlerS = this.buildRequest(cc, req);
		if (handlerS == null) {
			throw new JcpsException("handler is null.");
		}
		JsonElement res = cc.getConsole().getServer().handle(handlerS, req);
		cc.consume();
		return res;

	}

	protected abstract String buildRequest(CommandContext cc, JsonObject req);

}
