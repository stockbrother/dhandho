package cc.dhandho.input;

import java.io.IOException;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import cc.dhandho.RtException;

public class CsvRowWithHeader extends CsvRow {

	public static CsvRowWithHeader nextRow(int num, CSVReader reader, Map<String, Integer> headerMap)
			throws IOException {
		String[] line = reader.readNext();
		if (line == null) {
			return null;
		}
		return new CsvRowWithHeader(num, line, headerMap);
	}

	Map<String, Integer> headerMap;

	public CsvRowWithHeader(int num, String[] line, Map<String, Integer> headerMap) {
		super(num, line);
		this.headerMap = headerMap;
	}

	public String get(String name, boolean force) {
		Integer idx = headerMap.get(name);
		if (idx == null) {
			if (force) {
				throw new RtException("no column:" + name);
			} else {
				return null;
			}
		}
		return this.getString(idx, force);
	}

}
