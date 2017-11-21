package cc.dhandho.importer;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public abstract class AbstractHeaderCsvFileHandler {

	public void execute(Reader freader) {
		try {
			CSVReader reader = new CSVReader(freader);
			try {
				// skip header1
				String[] next = reader.readNext();
				Map<String, Integer> colIndexMap = new HashMap<>();
				for (int i = 0; i < next.length; i++) {
					String key = next[i];
					colIndexMap.put(key, i);
				}

				while (true) {
					next = reader.readNext();
					if (next == null) {
						break;
					}

					handleRow(next, colIndexMap);

				}

			} finally {
				reader.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract void handleRow(String[] next, Map<String, Integer> colIndexMap);
}
