package cc.dhandho.commons.commandline;

import java.util.List;

/**
 * 
 * @author wuzhen
 * 
 */
public interface CommandLineApplication {

	public CommandLineApplication prompt(String prompt);

	public CommandLineApplication pushReader(CommandLineReader cr);

	public CommandLineApplication pushReader(CommandLineReader cr, boolean popWhenClosed);

	public CommandLineApplication pushWriter(CommandLineWriter cw);

	public CommandLineWriter popWriter();

	public CommandLineWriter peekWriter();

	public List<CommandType> getCommandList();

	public CommandType getCommand(String name);

	public void addCommand(String name, CommandType type);

	// is print one more line after command print end.
	public void printLine(boolean printLine);

	public String getAttribute(String key);

	public void start();

	public void shutdown();
}
