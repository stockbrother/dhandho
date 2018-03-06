package cc.dhandho.rest.handler;

import java.io.IOException;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.container.Container;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.ReportMetaInfos;
import cc.dhandho.report.CorpDatedMetricReportData;
import cc.dhandho.report.MetricDefines;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.query.ReportDataMetricQuery;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.util.JsonUtil;

/**
 * 
 * @author Wu
 *
 */
public class ReportDataJsonHandler extends AbstractRestRequestHandler {
	ReportEngine reportEngine;
	protected MetricDefines metricDefines;
	protected ReportMetaInfos metaInfos;
	protected DbProvider dbProvider;
	@Override
	public void setContainer(Container app) {	
		super.setContainer(app);
		this.dbProvider = app.findComponent(DbProvider.class, true);
		this.reportEngine = app.findComponent(ReportEngine.class, true);
		metricDefines = app.findComponent(MetricDefines.class, true);
		metaInfos = app.findComponent(ReportMetaInfos.class, true);
	}

	@Override
	public void handle(RestRequestContext rrc) {

		JsonObject json = (JsonObject) rrc.parseReader();
		String corpId = json.get("corpId").getAsString();
		JsonArray metricA = json.get("metrics").getAsJsonArray();
		String[] metrics = JsonUtil.getAsStringArray(metricA);
		int[] years = new int[] { 2016, 2015, 2014, 2013, 2012 };

		AllQuotesInfos aqis = this.app.findComponent(AllQuotesInfos.class, true);

		// TODO
		Double value = aqis.getBuyPrice(corpId);
		int year = 2016;
		Double value2 = this.reportEngine.getMetricValue(corpId, year, "实收资本_或股本_");
		Double totoalPrice = ((value == null || value2 == null) ? null : value.doubleValue() * value2.doubleValue());//

		JsonObject jsonReq = this.metricDefines.buildMetricRequestAsJson(corpId, years, metrics);

		ReportDataMetricQuery q = new ReportDataMetricQuery(jsonReq, this.metaInfos);
		CorpDatedMetricReportData rdata = q.query(this.dbProvider);
		CorpDatedMetricReportData rdata2 = rdata.clone();
		if (totoalPrice == null) {
			rdata2.dividBy(Double.NaN);
		} else {
			rdata2.dividBy(totoalPrice);
		}

		JsonWriter writer = rrc.getWriter();
		try {

			writer.beginObject();
			writer.name("corpId").value(corpId);
			writer.name("price").value(value);
			writer.name("capital").value(value2);
			writer.name("totoalPrice").value(totoalPrice);
			writer.name("report");
			rdata.writeToJson(writer);
			writer.name("report2");
			rdata2.writeToJson(writer);
			writer.endObject();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}

	}

}
