package cc.dhandho.admin;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import cc.dhandho.commons.commandline.AbstractComandLineApp;
import cc.dhandho.commons.commandline.CommandAndLine;
import cc.dhandho.commons.commandline.DefaultConsoleReader;
import cc.dhandho.commons.commandline.DefaultConsoleWriter;

public class AdminConsole extends AbstractComandLineApp {

	public AdminConsole() {
		this.pushReader(new DefaultConsoleReader(new InputStreamReader(System.in)));
		this.pushWriter(new DefaultConsoleWriter(new OutputStreamWriter(System.out)));

	}

	@Override
	public void processLine(CommandAndLine cl) {
		if (cl.getCommand().getName().equals("exit")) {
			System.exit(0);
		} else {
			this.peekWriter().writeLn("no command:" + cl.getCommand().getName());
		}

	}

}
