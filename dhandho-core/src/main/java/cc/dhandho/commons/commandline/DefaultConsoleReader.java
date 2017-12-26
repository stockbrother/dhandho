/**
 * Dec 19, 2013
 */
package cc.dhandho.commons.commandline;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author Wu
 * 
 */
public class DefaultConsoleReader implements CommandLineReader {

	private Reader r;

	public DefaultConsoleReader() {
		this(new InputStreamReader(System.in));
	}

	public DefaultConsoleReader(Reader reader) {
		this.r = reader;
	}

	@Override
	public String readLine() {

		StringBuffer sb = new StringBuffer();
		while (true) {

			try {
				char c = (char) r.read();
				if (c == '\r') {
					// ignore?
					continue;
				}
				if (c == '\n') {
					break;
				}
				sb.append(c);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return sb.toString();
	}

	@Override
	public void close() {

	}

}
