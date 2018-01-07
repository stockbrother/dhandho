package cc.dhandho.report.impl;

import java.io.StringReader;

import com.age5k.jcps.framework.container.Container;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import cc.dhandho.ReportMetaInfos;
import cc.dhandho.graphdb.handler.CorpNameQueryHandler;
import cc.dhandho.report.CorpDatedMetricReportData;
import cc.dhandho.report.CorpDupontProfileReport;
import cc.dhandho.report.MetricDefines;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.query.JsonArrayMetricsQuery;
import cc.dhandho.report.query.ReportDataMetricQuery;
import cc.dhandho.rest.server.DbProvider;

public class ReportEngineImpl implements ReportEngine, Container.Aware {

	private DbProvider dbProvider;

	private ReportMetaInfos reportMetaInfos;

	private MetricDefines metricDefines;

	public ReportEngineImpl() {

	}

	@Override
	public void setContainer(Container app) {
		this.dbProvider = app.findComponent(DbProvider.class, true);
		this.reportMetaInfos = app.findComponent(ReportMetaInfos.class, true);
		this.metricDefines = app.findComponent(MetricDefines.class, true);
	}

	@Override
	public Double getMetricValue(String corpId, int year, String metric) {
		JsonObject json = new JsonObject();
		json.addProperty("corpId", corpId);
		JsonArray dates = new JsonArray();
		dates.add(year);// TODO
		json.add("dates", dates);
		JsonArray metrics = new JsonArray();
		metrics.add(metric);
		json.add("metrics", metrics);

		JsonReader reader = new JsonReader(new StringReader(json.toString()));
		JsonArrayMetricsQuery jb = new JsonArrayMetricsQuery(reader, this.reportMetaInfos);

		JsonArray mvalues = jb.query(dbProvider);
		Double d = null;
		if (mvalues.size() > 0) {

			JsonObject row1 = (JsonObject) mvalues.get(0);

			JsonElement m1 = row1.get("m1");
			d = m1.isJsonNull() ? null : m1.getAsDouble();
		}
		return d;
	}

	@Override
	public CorpDatedMetricReportData getReport(String corpId, int[] years, String[] metrics) {

		JsonObject json = this.metricDefines.buildMetricRequestAsJson(corpId, years, metrics);

		return new ReportDataMetricQuery(json, this.reportMetaInfos).query(this.dbProvider);
	}

	@Override
	public CorpDupontProfileReport getDupontProfileReport(String corpId, int[] years) {

		return null;
	}

	@Override
	public String getCorpName(String corpId) {
		return this.dbProvider.executeWithDbSession(new CorpNameQueryHandler(corpId));		
	}

}
