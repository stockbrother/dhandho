/**
 * Dec 19, 2013
 */
package cc.dhandho.commons.commandline;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * @author Wu
 * 
 */
public class DefaultConsoleWriter implements CommandLineWriter {
	// TODO out configurable.
	private Writer target = new OutputStreamWriter(System.out);

	public static String LBK = "\r\n";

	public DefaultConsoleWriter() {

	}

	@Override
	public CommandLineWriter writeLine(String line) {
		return this.write(line + LBK);
	}

	@Override
	public CommandLineWriter write(String str) {
		try {
			this.target.write(str, 0, str.length());
			this.target.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	@Override
	public CommandLineWriter writeLine() {

		return this.write(LBK);

	}

}
