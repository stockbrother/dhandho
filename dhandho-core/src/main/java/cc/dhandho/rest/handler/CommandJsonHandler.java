package cc.dhandho.rest.handler;

import com.age5k.jcps.framework.container.Container;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.dhandho.command.CommandExecutor;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;

public class CommandJsonHandler extends AbstractRestRequestHandler {

	CommandExecutor engine;

	@Override
	public void setContainer(Container app) {
		super.setContainer(app);
		this.engine = app.findComponent(CommandExecutor.class, true);
	}

	@Override
	public void handle(RestRequestContext arg0) {
		JsonObject json = arg0.parseReader();
		String command = json.get("command").getAsString();

		JsonElement res = engine.execute(command);
		arg0.write(res);
	}

}
