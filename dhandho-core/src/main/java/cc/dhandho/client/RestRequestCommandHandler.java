package cc.dhandho.client;

import com.google.gson.JsonObject;

import cc.dhandho.client.handler.DhandhoCommandHandler;
import cc.dhandho.client.rest.RestResponseContext;
import cc.dhandho.client.rest.RestResponseHandler;

public abstract class RestRequestCommandHandler extends DhandhoCommandHandler {

	public RestRequestCommandHandler() {

	}

	@Override
	public void execute(CommandContext cc) {
		JsonObject req = new JsonObject();
		String handlerS = this.buildRequest(cc, req);
		if (handlerS == null) {
			return;
		} else {
			JsonObject res = (JsonObject) cc.getServer().handle(handlerS, req);
			RestResponseContext rrc = new RestResponseContext(cc, req, res);
			rrc.handler = handlerS;
			this.onResponse(rrc);
			cc.consume();
		}
	}

	protected abstract String buildRequest(CommandContext cc, JsonObject req);

	protected void onResponse(RestResponseContext rrc) {

		RestResponseHandler rr = rrc.getConsole().getRestResponseHandler();
		rr.handle(rrc);
	}

}
