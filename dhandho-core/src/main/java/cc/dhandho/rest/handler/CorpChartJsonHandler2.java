package cc.dhandho.rest.handler;

import java.io.IOException;
import java.util.Iterator;

import com.age5k.jcps.JcpsException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.OResultSetHandler;
import cc.dhandho.rest.RestRequestContext;

@Deprecated
public class CorpChartJsonHandler2 extends DbSessionJsonHandler {

	@Override
	public void execute(RestRequestContext rrc, ODatabaseSession db) throws IOException {
		JsonWriter writer = rrc.getWriter();
		JsonElement json = Streams.parse(rrc.getReader());
		JsonObject obj = json.getAsJsonObject();
		String corpId = obj.get("corpId").getAsString();
		JsonArray components = obj.get("components").getAsJsonArray();
		StringBuffer sql = parseSql(components);
		writer.beginObject();
		writer.name("corpId").value(corpId);

		String query = "select corpId,reportDate,d_1,d_2 from balsheet where corpId=? limit 15";

		writer.name("values");

		DbUtil.executeQuery(db, query, new Object[] { corpId }, new OResultSetHandler<Void>() {

			@Override
			public Void handle(OResultSet rst) {
				try {
					writer.beginArray();
					while (rst.hasNext()) {

						writer.beginObject();
						OResult row = rst.next();
						Double d1 = row.getProperty("d_1");
						Double d2 = row.getProperty("d_2");
						writer.name("d1").value(d1);
						writer.name("d2").value(d2);
						writer.endObject();
					}
					writer.endArray();
				} catch (IOException e) {
					throw JcpsException.toRtException(e);
				}
				return null;
			}
		});

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
