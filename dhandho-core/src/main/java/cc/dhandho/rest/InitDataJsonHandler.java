package cc.dhandho.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import au.com.bytecode.opencsv.CSVReader;
import cc.dhandho.RtException;

/**
 * Client Init Handler,load metrics define from csv files.
 * 
 * @author Wu
 *
 */
public class InitDataJsonHandler implements JsonHandler {

	@Override
	public void execute(Gson gson, JsonReader reader, JsonWriter writer) throws IOException {
		writer.beginObject();
		writer.name("metric-define-table");
		this.loadTableCsv("cc/dhandho/rest/metric-define-table.csv", gson, reader, writer, true);

		writer.name("metric-define-group-table");
		this.loadTableCsv("cc/dhandho/rest/metric-define-group-table.csv", gson, reader, writer, false);
		writer.endObject();
	}

	private void loadTableCsv(String resource, Gson gson, JsonReader reader, JsonWriter writer, boolean hasHeader)
			throws IOException {

		InputStream is = this.getClass().getClassLoader().getResourceAsStream(resource);
		if (is == null) {
			throw new RtException("resource not found:" + resource);
		}
		writer.beginObject();

		CSVReader cReader = new CSVReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		try {
			Column[] columns = null;
			if (hasHeader) {
				writer.name("header");
				String[] header = cReader.readNext();
				writer.beginArray();

				columns = new Column[header.length];
				for (int i = 0; i < header.length; i++) {
					Column col = Column.valueOf(header[i]);
					writer.value(col.name);
					columns[i] = col;
				}

				writer.endArray();
			}

			writer.name("body");
			writer.beginArray();
			while (true) {
				String[] line = cReader.readNext();
				if (line == null) {
					break;
				}
				writer.beginArray();
				for (int i = 0; i < line.length; i++) {
					if (columns != null) {
						columns[i].writeValue(writer, line[i]);
					} else {
						writer.value(line[i]);
					}
				}
				writer.endArray();
			}
			writer.endArray();
		} finally {
			cReader.close();
		}

		writer.endObject();

	}

	private static abstract class Column {
		private String name;

		Column(String name) {
			this.name = name;
		}

		public abstract void writeValue(JsonWriter writer, String string) throws IOException;

		public static Column valueOf(String header) {
			int idx = header.indexOf("/");
			String name = header;
			String type = "String";
			if (idx >= 0) {
				name = name.substring(0, idx);
				type = header.substring(idx + 1);
			}

			if (type.equals("String")) {
				return new StringColumn(name);
			} else if (type.equals("Integer")) {
				return new IntegerColumn(name);
			} else if (type.equals("Boolean")) {
				return new BooleanColumn(name);
			} else {
				throw new RtException("type not supported:" + type);
			}
		}
	}

	private static class StringColumn extends Column {

		public StringColumn(String name) {
			super(name);
		}

		@Override
		public void writeValue(JsonWriter writer, String string) throws IOException {
			writer.value(string);
		}

	}

	private static class IntegerColumn extends Column {

		public IntegerColumn(String name) {
			super(name);
		}

		@Override
		public void writeValue(JsonWriter writer, String string) throws IOException {
			writer.value(Integer.parseInt(string));
		}
	}

	private static class BooleanColumn extends Column {

		public BooleanColumn(String name) {
			super(name);
		}

		@Override
		public void writeValue(JsonWriter writer, String string) throws IOException {
			boolean value = "Y".equalsIgnoreCase(string) || "true".equalsIgnoreCase(string);
			writer.value(value);
		}
	}
}
