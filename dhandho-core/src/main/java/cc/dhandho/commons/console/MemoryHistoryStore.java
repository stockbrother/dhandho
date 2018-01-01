package cc.dhandho.commons.console;

import java.util.ArrayList;
import java.util.List;

public class MemoryHistoryStore implements Console.HistoryStore {

	protected List<String> lineList = new ArrayList<String>();

	public String elementAt(int idx) {
		return this.lineList.get(idx);
	}

	public String getLast() {

		String rt = null;
		if (this.size() > 0) {
			rt = this.elementAt(this.size() - 1);
		}
		return rt;
	}

	@Override
	public void addElement(String line) {
		String last = this.getLast();
		if (line.equals(last)) {
			return;
		}
		this.lineList.add(line);
	}

	public int size() {
		return this.lineList.size();
	}

	@Override
	public void load() {

	}

	@Override
	public void save() {

	}
}
