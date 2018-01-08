package cc.dhandho.rest.handler;

import com.google.gson.JsonObject;

import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;

public class MyCorpJsonHandler extends AbstractRestRequestHandler {

	@Override
	public void handle(RestRequestContext arg0) {
		JsonObject json = (JsonObject) arg0.parseReader();
		String command = json.get("command").getAsString();
		if ("add".equals(command)) {
			
			
			
		} else {

		}

	}

}
