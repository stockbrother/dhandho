package cc.dhandho.commons.console.jfx;

import cc.dhandho.commons.console.Console;
import cc.dhandho.commons.console.Console.HistoryStore;

public class ConsoleHistory {
	
	JfxConsolePane console;
	
	HistoryStore store;


	public ConsoleHistory(JfxConsolePane console, Console.HistoryStore store) {
		// this.dataDir = dataDir;
		this.console = console;
		this.store = store;
	}


}
