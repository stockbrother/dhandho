package cc.dhandho.rest;

import java.io.IOException;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.AppContext;

public class CorpChartJsonHandler2 extends  AppContextAwareJsonHandler {

	@Override
	public void execute(Gson gson, JsonReader reader, JsonWriter writer) throws IOException {
		JsonElement json = Streams.parse(reader);
		JsonObject obj = json.getAsJsonObject();
		String corpId = obj.get("corpId").getAsString();
		JsonArray components = obj.get("components").getAsJsonArray();
		StringBuffer sql = parseSql(components);
		writer.beginObject();
		writer.name("corpId").value(corpId);

		ODatabaseSession db = app.openDB();
		try {
			String query = "select corpId,reportDate,d_1,d_2 from balsheet where corpId=? limit 15";
			OResultSet set = db.query(query, corpId);
			writer.name("values");
			try {

				writer.beginArray();
				while (set.hasNext()) {

					writer.beginObject();
					OResult row = set.next();
					Double d1 = row.getProperty("d_1");
					Double d2 = row.getProperty("d_2");
					writer.name("d1").value(d1);
					writer.name("d2").value(d2);
					writer.endObject();
				}
				writer.endArray();
			} finally {
				set.close();
			}
		} finally {
			db.close();
		}

		writer.endObject();

	}

	private StringBuffer parseSql(JsonArray jsonArray) {
		StringBuffer rt = new StringBuffer();

		Iterator<JsonElement> iterator = jsonArray.iterator();
		while (iterator.hasNext()) {
			JsonElement jI = iterator.next();
			if (jI.isJsonPrimitive()) {
				String name = jI.getAsString();
			} else if (jI.isJsonObject()) {
				JsonObject component = jI.getAsJsonObject();

			}

		}
		return rt;
	}

	private void queryChart(String corpId) {

	}

	public static class ComponentJsonContext {

	}

	public static class LeafComponentJsonContext extends ComponentJsonContext {
		String name;

		public static LeafComponentJsonContext valueOf(String name) {
			LeafComponentJsonContext rt = new LeafComponentJsonContext();
			rt.name = name;
			return rt;
		}
	}

	public static class ObjectComponentJsonContext extends ComponentJsonContext {
		JsonObject component;
		String name;
		int offset;
		String operator;
		JsonArray components;

		public static ObjectComponentJsonContext valueOf(JsonObject component) {
			ObjectComponentJsonContext rt = new ObjectComponentJsonContext();
			rt.component = component;
			rt.name = component.get("name").getAsString();
			rt.offset = component.get("offset").getAsInt();
			rt.operator = component.get("operator").getAsString();
			rt.components = component.get("components").getAsJsonArray();
			return rt;
		}

	}

}
