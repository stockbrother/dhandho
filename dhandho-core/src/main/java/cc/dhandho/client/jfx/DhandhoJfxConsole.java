package cc.dhandho.client.jfx;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import com.age5k.jcps.JcpsException;
import cc.dhandho.client.DhandhoCliConsole;
import cc.dhandho.commons.commandline.CommandLineWriter;
import cc.dhandho.commons.commandline.DefaultConsoleReader;
import cc.dhandho.commons.console.jfx.DefaultHistoryStore;
import cc.dhandho.commons.console.jfx.JfxConsolePane;

/**
 * GUI console implementation.
 * 
 * @author wu
 *
 */
public class DhandhoJfxConsole extends DhandhoCliConsole implements CommandLineWriter {

	protected JfxConsolePane consolePane;

	public DhandhoJfxConsole(FileObject consoleHome) {
		super(consoleHome);
		try {
			FileObject historyFile = consoleHome.resolveFile("history.txt");
			consolePane = new JfxConsolePane(new DefaultHistoryStore(historyFile));
		} catch (FileSystemException e) {
			throw JcpsException.toRtException(e);
		}

		this.pushReader(new DefaultConsoleReader(consolePane.getReader()));
		this.pushWriter(this);
	}

	@Override
	public CommandLineWriter writeLn() {
		consolePane.println();
		return this;
	}

	@Override
	public CommandLineWriter write(String str) {
		consolePane.print(str);
		return this;
	}

	@Override
	public CommandLineWriter writeLn(String line) {
		consolePane.println(line);
		return this;
	}

	@Override
	public CommandLineWriter write(int value) {
		consolePane.print(String.valueOf(value));//
		return this;
	}
	@Override
	public void clear() {
		consolePane.clear();
	}

	@Override
	public void shutdown() {		
		this.consolePane.close();
		super.shutdown();
	}
	
}