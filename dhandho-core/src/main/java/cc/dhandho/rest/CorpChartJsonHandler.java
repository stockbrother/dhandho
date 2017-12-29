package cc.dhandho.rest;

import java.io.IOException;
import java.io.StringWriter;

import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.DbAliasInfos;
import cc.dhandho.report.SvgChartMetricQueryBuilder;

/**
 * 
 * @author Wu
 *
 */
public class CorpChartJsonHandler extends DbSessionJsonHandler {

	@Override
	public void execute(RestRequestContext rrc, ODatabaseSession db) throws IOException {

		JsonWriter writer = rrc.getWriter();

		DbAliasInfos aliasInfos = new DbAliasInfos();

		aliasInfos.initialize(db);

		StringWriter swriter = new StringWriter();

		SvgChartMetricQueryBuilder svgQ = new SvgChartMetricQueryBuilder(rrc.getReader(), aliasInfos);
		svgQ.query(db, swriter);
		int width = svgQ.query.getWidth();
		int height = svgQ.query.getHeight();
		writer.beginObject();
		writer.name("width").value(width);
		writer.name("height").value(height);
		writer.name("svg");
		writer.value(swriter.toString());

		writer.endObject();

	}

}
