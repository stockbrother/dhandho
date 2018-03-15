package cc.dhandho.rest.handler;

import com.age5k.jcps.framework.container.Container;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.ReportMetaInfos;
import cc.dhandho.report.MetricDefines;
import cc.dhandho.report.query.SvgChartMetricQueryBuilder;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.util.JsonUtil;

/**
 * A list of charts with SVG format.
 * @author Wu
 *
 */

public class StockChartsJsonHandler extends AbstractRestRequestHandler {

	AllQuotesInfos aqis;
	ReportMetaInfos aliasInfos;
	DbProvider dbProvider;
	MetricDefines metricDefines;

	@Override
	public void setContainer(Container app) {

		super.setContainer(app);
		this.dbProvider = app.findComponent(DbProvider.class, true);
		aliasInfos = this.app.findComponent(ReportMetaInfos.class, true);
		this.metricDefines = this.app.findComponent(MetricDefines.class, true);
	}

	@Override
	public void handleInternal(RestRequestContext rrc) throws Exception {
		JsonObject req = rrc.parseReader();
		String corpId = req.get("corpId").getAsString();
		JsonArray ys = req.get("years").getAsJsonArray();
		int[] years = JsonUtil.getAsIntArray(ys);
		JsonArray mA = req.get("metricsArray").getAsJsonArray();
		JsonWriter writer = rrc.getWriter();
		writer.beginObject();
		writer.name("type").value("charts");
		writer.name("charts").beginArray();
		for (int i = 0; i < mA.size(); i++) {
			JsonArray metricsJ = mA.get(i).getAsJsonArray();
			String[] metrics = JsonUtil.getAsStringArray(metricsJ);
			JsonObject query = this.metricDefines.buildMetricRequestAsJson(corpId, years, metrics);
			SvgChartMetricQueryBuilder r = SvgChartMetricQueryBuilder.newInstance(query, aliasInfos);
			StringBuffer sb = r.query(this.dbProvider);
			writer.value(sb.toString());
		}
		writer.endArray();
		writer.endObject();

	}

}
