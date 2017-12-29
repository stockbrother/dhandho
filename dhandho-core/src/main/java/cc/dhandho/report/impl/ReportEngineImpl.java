package cc.dhandho.report.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.AppContext;
import cc.dhandho.Processor;
import cc.dhandho.ReportMetaInfos;
import cc.dhandho.RtException;
import cc.dhandho.report.JsonMetricSqlLinkQueryBuilder;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.server.DbProvider;
import cc.dhandho.util.JsonUtil;

public class ReportEngineImpl implements ReportEngine, AppContext.Aware {

	private DbProvider dbProvider;

	private ReportMetaInfos reportMetaInfos;

	public ReportEngineImpl() {

	}

	@Override
	public void setAppContext(AppContext app) {
		this.dbProvider = app.findComponent(DbProvider.class, true);
		this.reportMetaInfos = app.findComponent(ReportMetaInfos.class, true);
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
		JsonMetricSqlLinkQueryBuilder jb = new JsonMetricSqlLinkQueryBuilder(reader, this.reportMetaInfos);
		StringWriter sWriter = new StringWriter();
		JsonWriter writer = new JsonWriter(sWriter);
		this.dbProvider.executeWithDbSession(new Processor<ODatabaseSession>() {

			@Override
			public void process(ODatabaseSession db) {
				try {
					jb.build().query(db, writer);
				} catch (IOException e) {
					throw RtException.toRtException(e);
				}
			}
		});

		JsonArray mvalues = (JsonArray) JsonUtil.parse(sWriter.getBuffer().toString());

		JsonObject row1 = (JsonObject) mvalues.get(0);

		Double d = row1.get("m1").getAsDouble();

		return d;
	}

}
