package cc.dhandho.io;

import java.io.Reader;

public interface FromReader {
	
	public Object read(Reader reader);
	
	public void flush();
}
