package cc.dhandho.client;

import cc.dhandho.commons.commandline.CommandAndLine;
import cc.dhandho.commons.commandline.CommandLineWriter;

public abstract class DhandhoCommandHandler implements CommandHandler {

	@Override
	public void execute(CommandAndLine line) {
		this.execute(line, (DhandhoCliConsole) line.getConsole(), line.getConsole().peekWriter());
	}

	protected abstract void execute(CommandAndLine line, DhandhoCliConsole console, CommandLineWriter writer);

}
