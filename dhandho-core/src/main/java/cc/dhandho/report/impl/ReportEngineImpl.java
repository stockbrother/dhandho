package cc.dhandho.report.impl;

import java.io.StringReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import cc.dhandho.ReportMetaInfos;
import cc.dhandho.commons.container.Container;
import cc.dhandho.report.MetricDefines;
import cc.dhandho.report.ReportData;
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

		JsonObject row1 = (JsonObject) mvalues.get(0);

		Double d = row1.get("m1").getAsDouble();

		return d;
	}

	@Override
	public ReportData getReport(String corpId, int[] years, String[] metrics) {

		JsonObject json = this.metricDefines.buildMetricRequestAsJson(corpId, years, metrics);
		
		return new ReportDataMetricQuery(json, this.reportMetaInfos).query(this.dbProvider);
	}

}
