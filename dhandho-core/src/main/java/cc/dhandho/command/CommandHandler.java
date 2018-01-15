package cc.dhandho.command;

import com.google.gson.JsonElement;

public interface CommandHandler {

	public JsonElement execute(CommandContext cc);

}
