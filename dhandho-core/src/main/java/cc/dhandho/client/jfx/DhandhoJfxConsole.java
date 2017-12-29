package cc.dhandho.client.jfx;

import cc.dhandho.client.DhandhoCliConsole;
import cc.dhandho.commons.commandline.CommandLineWriter;
import cc.dhandho.commons.commandline.DefaultConsoleReader;
import cc.dhandho.commons.jfx.ConsolePane;

/**
 * GUI console implementation.
 * 
 * @author wu
 *
 */
public class DhandhoJfxConsole extends DhandhoCliConsole implements CommandLineWriter {

	protected ConsolePane consolePane;

	public DhandhoJfxConsole() {
		consolePane = new ConsolePane();

		this.pushReader(new DefaultConsoleReader(consolePane.getReader()));
		this.pushWriter(this);
	}

	@Override
	public CommandLineWriter writeLine() {
		consolePane.println();
		return this;
	}

	@Override
	public CommandLineWriter write(String str) {
		consolePane.print(str);
		return this;
	}

	@Override
	public CommandLineWriter writeLine(String line) {
		consolePane.println(line);
		return this;
	}

	@Override
	public CommandLineWriter write(int value) {
		consolePane.print(String.valueOf(value));//
		return this;
	}
	@Override
	public void clear() {
		consolePane.clear();
	}
}