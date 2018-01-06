/**
 * Dec 19, 2013
 */
package cc.dhandho.commons.commandline;

/**
 * @author Wu
 * 
 */
public interface CommandLineReader {

	/**
	 * 
	 * @return null if reader is closed.
	 */
	public String readLine();

	public void close();

}
