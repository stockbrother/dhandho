package cc.dhandho.rest;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.RtException;
import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.GDBResultSetProcessor;
import cc.dhandho.util.DbInitUtil;

public class CorpInfoJsonHandler extends AppContextAwareJsonHandler {

	@Override
	public void execute(Gson gson, JsonReader reader, JsonWriter writer) throws IOException {

		JsonElement json = Streams.parse(reader);
		JsonObject query = json.getAsJsonObject();
		String corpId = query.get("corpId").getAsString();

		ODatabaseSession db = app.openDB();
		String sql = "select from " + DbInitUtil.V_CORP_INFO + " where corpId=?";

		writer.beginObject();
		DbUtil.executeQuery(db, sql, new Object[] { corpId }, new GDBResultSetProcessor<Void>() {

			@Override
			public Void process(OResultSet rst) {

				if (rst.hasNext()) {
					OVertex v = rst.next().getVertex().get();
					String category = v.getProperty("category");
					String corpName = v.getProperty("corpName");
					try {

						writer.name("corpId").value(corpId);
						writer.name("corpName").value(corpName);
						writer.name("category").value(category);
					} catch (IOException e) {
						throw new RtException(e);
					}

				}

				return null;
			}
		});
		writer.endObject();

	}

	private void queryChart(String corpId) {

	}

}
