package cc.dhandho.client;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;

import cc.dhandho.commons.commandline.CommandType;

public class Usage {

	public void usageOfCommand(CommandContext cl, CommandType cmd) {
		HelpFormatter formatter = new HelpFormatter();
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		formatter.printHelp(pw, 1100, cmd.getName(), cmd.getDescription(), cmd.getOptions(), 2, 12, " ", true);
		cl.getWriter().write(sw.getBuffer().toString());
	}

	public void usageOfAll(CommandContext cl) {
		
		cl.getWriter().writeLn("usage: help <command>");
		cl.getWriter().writeLn("all available commands are:");
		// cc.writeLine("The most commonly used commands are:");
		List<CommandType> cmdL = cl.getConsole().getCommandList();
		int maxLength = this.getMaxLength(cmdL);
		for (int i = 0; i < cmdL.size(); i++) {
			CommandType cmd = cmdL.get(i);
			StringBuilder line = new StringBuilder();
			String name = cmd.getName();
			line.append(" ");
			line.append(name);
			for (int x = name.length(); x < maxLength; x++) {
				line.append(" ");
			}
			line.append(" ");

			int leftColumnSize = line.length();
			String[] descA = cmd.getDescription().split("\n");
			for (int j = 0; j < descA.length; j++) {

				if (j > 0) {
					line = new StringBuilder();
					for (int x = 0; x < leftColumnSize; x++) {
						line.append(" ");
					}
				}
				line.append(descA[j]);				

				cl.getWriter().writeLn(line.toString());
			}			
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
