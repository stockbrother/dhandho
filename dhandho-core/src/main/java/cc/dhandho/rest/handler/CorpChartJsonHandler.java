package cc.dhandho.rest.handler;

import java.io.IOException;

import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.report.query.SvgChartMetricQueryBuilder;
import cc.dhandho.rest.RestRequestContext;

/**
 * 
 * @author Wu
 *
 */
public class CorpChartJsonHandler extends DbSessionJsonHandler {

	@Override
	public void execute(RestRequestContext rrc, ODatabaseSession db) throws IOException {

		JsonWriter writer = rrc.getWriter();

		DbReportMetaInfos aliasInfos = new DbReportMetaInfos();

		aliasInfos.initialize(db);

		SvgChartMetricQueryBuilder svgQ = new SvgChartMetricQueryBuilder(rrc.getReader(), aliasInfos);
		StringBuffer sb = svgQ.query(db);
		
		int width = 600;
		int height = 400;
		writer.beginObject();
		writer.name("width").value(width);
		writer.name("height").value(height);
		writer.name("svg");
		writer.value(sb.toString());

		writer.endObject();

	}

}
