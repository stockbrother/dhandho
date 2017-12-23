/**
 * Dec 19, 2013
 */
package cc.dhandho.commons.commandline;

/**
 * @author Wu
 * 
 */
public interface CommandLineWriter {
	
	public CommandLineWriter writeLine();	
	
	public CommandLineWriter write(String str);
	
	public CommandLineWriter writeLine(String line);
	
}
