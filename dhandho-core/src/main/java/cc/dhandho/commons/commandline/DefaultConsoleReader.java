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

	private DefaultConsoleReader() {
		this(new InputStreamReader(System.in));
	}

	public DefaultConsoleReader(Reader reader) {
		this.r = reader;
	}

	@Override
	public String readLine() {

		StringBuffer sb = new StringBuffer();
		boolean eof = true;
		try {
			while (true) {

				int b = r.read();
				if (b == -1) {
					eof = true;
					break;
				}
				char c = (char) b;

				if (c == '\r') {
					// ignore?
					continue;
				}
				if (c == '\n') {
					break;
				}
				sb.append(c);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (eof && sb.length() == 0) {
			return null;
		}
		return sb.toString();
	}

	@Override
	public void close() {
//		try {
//			this.r.close();
//		} catch (IOException e) {
//			throw JcpsException.toRtException(e);
//		}
	}

}
