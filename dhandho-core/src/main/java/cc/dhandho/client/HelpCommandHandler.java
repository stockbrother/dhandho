package cc.dhandho.client;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;

import cc.dhandho.commons.commandline.CommandAndLine;
import cc.dhandho.commons.commandline.CommandLineWriter;
import cc.dhandho.commons.commandline.CommandType;

public class HelpCommandHandler implements CommandHandler {

	@Override
	public void execute(CommandAndLine line) {

		CommandAndLine cl = line;
		CommandLineWriter writer = cl.getConsole().peekWriter();
		String[] as = cl.getArgs();
		if (as.length == 1) {
			String cname = as[0];
			CommandType cmd = line.getConsole().getCommand(cname);
			if (cmd == null) {
				writer.writeLine();
				writer.writeLine("not found command:" + cname);
				return;
			}
			HelpFormatter formatter = new HelpFormatter();
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			formatter.printHelp(pw, 1100, cname, cmd.getDescription(), cmd.getOptions(), 2, 12, " ", true);
			writer.write(sw.getBuffer().toString());
		} else {
			this.printHelpItSelf(cl, writer);
		}

	}

	protected void printHelpItSelf(CommandAndLine cl, CommandLineWriter cc) {		
		cc.writeLine("usage: help <command>");
		cc.writeLine();
		//cc.writeLine("The most commonly used commands are:");
		List<CommandType> cmdL = cl.getConsole().getCommandList();
		int maxLength = this.getMaxLength(cmdL);
		for (int i = 0; i < cmdL.size(); i++) {
			CommandType cmd = cmdL.get(i);
			String name = cmd.getName();
			cc.write(" ");
			cc.write(name);
			for (int x = name.length(); x < maxLength; x++) {
				cc.write(" ");
			}
			cc.write(" ");
			cc.write(cmd.getDescription());
			cc.writeLine();
		}

	}
	
	private int getMaxLength(List<CommandType> cmdL) {
		int rt = 0;

		for (CommandType c : cmdL) {
			int l = c.getName().length();
			if (rt < l) {
				rt = l;
			}
		}
		return rt;
	}
}
