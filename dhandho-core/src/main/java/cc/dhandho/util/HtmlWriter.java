package cc.dhandho.util;

import java.io.IOException;
import java.io.Writer;

public class HtmlWriter extends Writer{
	
	Writer writer;
	
	public HtmlWriter(Writer writer) {
		this.writer = writer;
	}
	
	public HtmlWriter beginTable() throws IOException{
		this.append("<table>");
		return this;
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flush() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}
}
