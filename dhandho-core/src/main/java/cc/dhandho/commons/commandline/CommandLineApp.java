package cc.dhandho.commons.commandline;

import java.util.List;
import java.util.concurrent.Future;

/**
 * 
 * @author Wu
 * 
 */
public interface CommandLineApp {

	/**
	 * Set the prompt character.
	 * 
	 * @param prompt
	 * @return
	 */
	public CommandLineApp prompt(String prompt);

	public CommandLineApp pushReader(CommandLineReader cr);

	public CommandLineApp pushReader(CommandLineReader cr, boolean popWhenClosed);

	public CommandLineApp pushWriter(CommandLineWriter cw);

	public CommandLineWriter popWriter();

	public CommandLineWriter peekWriter();

	public List<CommandType> getCommandList();

	public CommandType getCommand(String name);

	public void addCommand(String name, CommandType type);

	// is print one more line after command print end.
	public void printLine(boolean printLine);

	public String getAttribute(String key);

	/**
	 * start the thread for running with.
	 */
	public Future<Object> start();

	/**
	 * Shutdown and block until finished.
	 */
	public void shutdown();

	/**
	 * Send message to shutdown it later.
	 */
	public void shutdownAsync();

	public boolean isAlive();

}
