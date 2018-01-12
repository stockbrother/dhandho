package cc.dhandho.client.rest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.dhandho.client.CommandContext;
import cc.dhandho.client.DhandhoCliConsole;

public class RestResponseContext {

	public JsonObject request;

	public JsonElement response;

	public CommandContext commandContext;

	public String handler;

	public RestResponseContext(CommandContext cc, JsonObject req, JsonElement res) {
		this.commandContext = cc;
		this.request = req;
		this.response = res;
	}

	public String getHandler() {
		return this.handler;
	}

	public DhandhoCliConsole getConsole() {
		return this.commandContext.getConsole();
	}

	public boolean isHandler(String name) { //
		return name.equals(this.handler);
	}

}
