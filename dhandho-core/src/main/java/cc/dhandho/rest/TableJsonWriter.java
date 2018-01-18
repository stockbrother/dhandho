package cc.dhandho.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TableJsonWriter {
	JsonObject json = new JsonObject();
	JsonArray columnNames = new JsonArray();
	JsonArray rowArray = new JsonArray();

	public TableJsonWriter() {
		json.addProperty("type", "table");		
		json.add("columnNames", columnNames);
		json.add("rowArray", rowArray);
	}

	public TableJsonWriter addRow(JsonElement[] rowA) {

		JsonArray row = new JsonArray();
		for (JsonElement col : rowA) {
			row.add(col);
		}
		return this.addRow(row);
	}

	public TableJsonWriter addRow(JsonArray row) {
		this.rowArray.add(row);
		return this;
	}

	public TableJsonWriter columns(String[] columns) {
		for (int i = 0; i < columns.length; i++) {
			this.columnNames.add(columns[i]);
		}
		return this;
	}

	public JsonObject getJson() {
		return this.json;
	}

}
