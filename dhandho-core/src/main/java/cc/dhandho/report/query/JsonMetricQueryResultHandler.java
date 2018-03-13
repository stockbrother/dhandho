package cc.dhandho.report.query;

import java.io.IOException;
import java.io.StringWriter;

import com.google.gson.JsonArray;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import com.age5k.jcps.JcpsException;
import cc.dhandho.graphdb.OResultSetHandler;
import cc.dhandho.util.JsonUtil;
/**
 * @see QueryJsonWrapper
 * @author Wu
 *
 */

public class JsonMetricQueryResultHandler implements OResultSetHandler<JsonArray> {

	public JsonMetricQueryResultHandler() {

	}

	@Override
	public JsonArray handle(OResultSet rst) {
		StringWriter sWriter = new StringWriter();
		JsonWriter writer = new JsonWriter(sWriter);
		try {
			writer.beginArray();
			while (rst.hasNext()) {
				writer.beginObject();
				OResult row = rst.next();

				for (String key : row.getPropertyNames()) {
					writer.name(key);
					Object value = row.getProperty(key);
					String jsonV = row.toJson(value);
					writer.jsonValue(jsonV);
				}

				writer.endObject();
			}
			writer.endArray();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
		return (JsonArray) JsonUtil.parse(sWriter.getBuffer().toString());
	}

}
