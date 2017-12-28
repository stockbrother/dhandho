package cc.dhandho.rest;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.RtException;
import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.GDBResultSetProcessor;
import cc.dhandho.util.DbInitUtil;

public class CorpInfoJsonHandler extends DbSessionJsonHandler {

	@Override
	public void execute(RestRequestContext rrc, ODatabaseSession db) throws IOException {
		JsonWriter writer = rrc.getWriter();
		JsonElement json = Streams.parse(rrc.getReader());
		JsonObject query = json.getAsJsonObject();
		String corpId = query.get("corpId").getAsString();

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

}
