package cc.dhandho.admin;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.age5k.jcps.framework.handler.Handler2;

import cc.dhandho.commons.commandline.AbstractComandLineApp;
import cc.dhandho.commons.commandline.CommandAndLine;
import cc.dhandho.commons.commandline.CommandType;
import cc.dhandho.commons.commandline.DefaultConsoleReader;
import cc.dhandho.commons.commandline.DefaultConsoleWriter;

public class AdminConsole extends AbstractComandLineApp {

	public AdminConsole() {
		this.pushReader(new DefaultConsoleReader(new InputStreamReader(System.in)));
		this.pushWriter(new DefaultConsoleWriter(new OutputStreamWriter(System.out)));
		this.addCommand(new CommandType("exit"), new Handler2<CommandAndLine>() {

			@Override
			public void handle(CommandAndLine arg0) {
				System.exit(0);				
			}
		});
	}

}
