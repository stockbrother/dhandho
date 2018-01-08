/**
 * Dec 19, 2013
 */
package cc.dhandho.commons.commandline;

/**
 * @author Wu
 * 
 */
public interface CommandLineWriter {
	
	public CommandLineWriter writeLn();	
	
	public CommandLineWriter write(String str);
	
	public CommandLineWriter write(int value);
	
	public CommandLineWriter writeLn(String line);
	
	
}
