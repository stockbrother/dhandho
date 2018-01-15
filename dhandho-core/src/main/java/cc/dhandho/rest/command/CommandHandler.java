package cc.dhandho.rest.command;

import com.google.gson.JsonElement;

public interface CommandHandler {

	public JsonElement execute(CommandContext cc);

}
