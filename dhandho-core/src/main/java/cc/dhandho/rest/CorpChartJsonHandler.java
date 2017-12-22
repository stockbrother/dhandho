package cc.dhandho.rest;

import java.io.IOException;
import java.io.StringWriter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.AppContext;
import cc.dhandho.DbAliasInfos;

/**
 * 
 * @author Wu
 *
 */
public class CorpChartJsonHandler extends AppContextAwareJsonHandler {

	@Override
	public void execute(Gson gson, JsonReader reader, JsonWriter writer) throws IOException {

		ODatabaseSession db = app.openDB();
		try {

			DbAliasInfos aliasInfos = new DbAliasInfos();

			aliasInfos.initialize(db);

			StringWriter swriter = new StringWriter();

			SvgChartMetricQueryBuilder svgQ = new SvgChartMetricQueryBuilder(reader, aliasInfos);
			svgQ.query(db, swriter);
			int width = svgQ.query.getWidth();
			int height = svgQ.query.getHeight();
			writer.beginObject();
			writer.name("width").value(width);
			writer.name("height").value(height);
			writer.name("svg");
			writer.value(swriter.toString());

			writer.endObject();
		} finally {

			db.close();
		}

	}

}
