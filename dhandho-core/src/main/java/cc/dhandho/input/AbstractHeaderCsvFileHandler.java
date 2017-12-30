package cc.dhandho.input;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

public abstract class AbstractHeaderCsvFileHandler {

	public static final Logger LOG = LoggerFactory.getLogger(AbstractHeaderCsvFileHandler.class);

	protected String name;

	public AbstractHeaderCsvFileHandler(String name) {
		this.name = name;
	}

	public void execute(Reader freader) {
		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("processing file:" + name);
			}
			
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
					if (LOG.isTraceEnabled()) {
						LOG.trace("handle row:" + Arrays.asList(next));
					}
					handleRow(next, colIndexMap);

				}

			} finally {
				reader.close();
			}

			if (LOG.isDebugEnabled()) {
				LOG.debug("done of processing file:" + name);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract void handleRow(String[] next, Map<String, Integer> colIndexMap);
}
