package cc.dhandho.commons.console;

import java.io.Reader;

public interface Console {

	public static interface HistoryStore {
		public void load();

		public void save();

		public void addElement(String line);

		public String elementAt(int idx);

		public int size();

	}

	public Reader getReader();

	public void enter(String line);

	public void println(Object o);

	public void print(final Object o);

	public void println();

	public void error(Object o);

	public void close();
}
