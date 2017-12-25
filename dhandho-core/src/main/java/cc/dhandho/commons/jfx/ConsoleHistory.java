package cc.dhandho.commons.jfx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ConsoleHistory {
	int histLine = 0;
	ConsolePane console;
	private int maxHistory = 1000;
	File dataDir;
	File historyFile;
	List<String> lineList = new ArrayList<String>();

	public ConsoleHistory(ConsolePane console) {
		//this.dataDir = dataDir;
		this.console = console;
		historyFile = new File(this.dataDir, "history");
	}

	public void load() {

		if (!historyFile.exists()) {
			return;
		}
		BufferedReader rd;
		try {
			rd = new BufferedReader(
					new InputStreamReader(new FileInputStream(this.historyFile), Charset.forName("utf-8")));
			while (true) {
				String line = rd.readLine();
				if (line == null) {
					break;
				}
				this.addElement(line);
			}
			rd.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public String getLast() {

		String rt = null;
		if (this.size() > 0) {
			rt = this.elementAt(this.size() - 1);
		}
		return rt;
	}

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

	public void save() {
		try {
			Writer wt = new OutputStreamWriter(new FileOutputStream(this.historyFile), Charset.forName("utf-8"));
			for (int i = 0; i < this.size(); i++) {
				String line = this.elementAt(i);
				wt.write(line);
				wt.write("\r\n");//
			}
			wt.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	void historyUp() {
		if (this.size() == 0) {
			return;
		}
		if (this.histLine == 0) {// save current line
			console.startedLine = console.getCmd();
		}

		if (this.histLine < this.size()) {
			this.histLine++;
			showHistoryLine();
		}
	}

	void historyDown() {
		if (histLine == 0) {
			return;
		}

		histLine--;
		showHistoryLine();
	}

	public String elementAt(int idx) {
		return this.lineList.get(idx);
	}

	private void showHistoryLine() {
		String showline;
		if (histLine == 0) {
			showline = console.startedLine;
		} else {
			showline = elementAt(size() - histLine);
		}

		console.replaceRange(showline, console.cmdStart, console.textLength());
		//console.text.setCaretPosition(console.textLength());
		//console.text.repaint();
	}
}
