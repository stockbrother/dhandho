package cc.dhandho.input;

import java.io.IOException;
import java.io.Reader;

import org.apache.commons.vfs2.FileObject;

import au.com.bytecode.opencsv.CSVReader;
import cc.dhandho.commons.vfs.FileReaderIterator;

public abstract class CsvReaderIterator extends FileReaderIterator {

	public CsvReaderIterator(FileObject dir) {
		super(dir);
	}

	@Override
	protected void onReader(String type, FileObject file, Reader freader, int number) throws IOException {
		CSVReader reader = new CSVReader(freader);
		try {

			int lineNumber = 0;

			while (true) {
				lineNumber++;
				String[] next = reader.readNext();
				if (next == null) {

					break;
				}
				onRow(new CsvRow(lineNumber, next));
			}
		} finally {
			reader.close();
		}

	}

	protected abstract void onRow(CsvRow row);

}
