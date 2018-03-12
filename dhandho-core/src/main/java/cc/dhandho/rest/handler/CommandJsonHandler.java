package cc.dhandho.rest.handler;

import com.age5k.jcps.framework.container.Container;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.rest.command.CommandExecutor;
/**
 * @see CommandExecutor
 * @author wu
 *
 */
public class CommandJsonHandler extends AbstractRestRequestHandler {

	CommandExecutor engine;

	@Override
	public void setContainer(Container app) {
		super.setContainer(app);
		this.engine = app.findComponent(CommandExecutor.class, true);
	}

	@Override
	public void handleInternal(RestRequestContext arg0) {
		JsonObject json = arg0.parseReader();
		String command = json.get("command").getAsString();

		JsonElement res = engine.execute(command);
		arg0.write(res);
	}

}
