/**
 * Dec 29, 2013
 */
package cc.dhandho.commons.commandline;

import org.apache.commons.cli.CommandLine;

/**
 * @author Wu
 * 
 */
public class CommandAndLine {

	private int index;
	private CommandType type;
	private CommandLine line;
	private CommandLineApplication console;

	/**
	 * @param command2
	 * @param cl
	 */
	public CommandAndLine(CommandLineApplication console, int idx, CommandType command2, CommandLine cl) {
		this.type = command2;
		this.console = console;
		this.line = cl;
		this.index = idx;
	}

	public CommandLineApplication getConsole() {
		return console;
	}

	public CommandType getCommand() {
		return type;
	}

	public CommandLine getLine() {
		return line;
	}

	/**
	 * @param c
	 * @return
	 */
	public boolean hasOption(char opt) {
		// TODO Auto-generated method stub
		return this.line.hasOption(opt);
	}

	/**
	 * @param c
	 * @return
	 */
	public String getOptionValue(char string) {
		return this.getOptionValue(String.valueOf(string));
	}

	public String getOptionValue(String string) {
		String value = this.line.getOptionValue(string);
		return this.resolve(value);
	}
	
	public String getOptionRawValue(String opt){
		return this.line.getOptionValue(opt);
	}

	public String resolve(String value) {
		if (!value.startsWith("$")) {
			return value;
		}
		String key = value.substring(1);
		String rt = this.console.getAttribute(key);
		if (rt == null) {
			throw new RuntimeException("cannot resolve var:" + key);
		}
		return rt;
	}

	/**
	 * @return
	 */
	public String[] getArgs() {
		// TODO Auto-generated method stub
		return this.line.getArgs();
	}

	/**
	 * @return
	 */
	public int getIndex() {
		// TODO Auto-generated method stub
		return index;
	}

}
