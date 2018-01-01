package cc.dhandho.commons.console.jfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.commons.console.MemoryHistoryStore;

public class DefaultHistoryStore extends MemoryHistoryStore {
	
	FileObject historyFile;
	
	public DefaultHistoryStore(FileObject file) {
		this.historyFile = file;
	}

	@Override
	public void load() {

		try {
			if (!historyFile.exists()) {
				return;
			}

			BufferedReader rd = new BufferedReader(
					new InputStreamReader(this.historyFile.getContent().getInputStream(), Charset.forName("utf-8")));
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


	@Override
	public void save() {
		try {
			Writer wt = new OutputStreamWriter(this.historyFile.getContent().getOutputStream(),
					Charset.forName("utf-8"));
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


}
