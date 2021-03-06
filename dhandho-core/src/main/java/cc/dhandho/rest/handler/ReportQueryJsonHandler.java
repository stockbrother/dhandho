package cc.dhandho.rest.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.age5k.jcps.JcpsException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.Quarter;
import cc.dhandho.ReportMetaInfos;
import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.OResultSetHandler;
import cc.dhandho.graphdb.dataver.DbUpgrader0_0_1;
import cc.dhandho.report.ReportItemLocator;
import cc.dhandho.report.ReportItemLocators;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.util.Visitor;

public class ReportQueryJsonHandler extends DbSessionJsonHandler {

	public static class ReportRow {
		List<Double> itemList = new ArrayList<>();

		private String key;

		private Integer columnIndex;

		ReportItemLocator locator;

		int size;

		public int getSize() {
			return size;
		}

		public String getKey() {
			return key;
		}

		public ReportRow(int size, String key, ReportItemLocator ri) {
			this.size = size;
			this.key = key;
			this.locator = ri;
		}

		public Double get(int idx) {
			if (idx >= this.itemList.size()) {
				return null;
			}
			return itemList.get(idx);
		}

		public Double getFirstNotNullItem() {
			for (Double eo : this.itemList) {
				if (eo != null) {
					return eo;
				}
			}
			return null;
		}

		public void set(int idx, Double eo) {
			while (idx >= itemList.size()) {
				itemList.add(null);
			}
			itemList.set(idx, eo);
		}

	}

	private String corpId;

	private Quarter quarter;

	ReportItemLocators.Group template;

	int years = 5;

	private List<ReportRow> newReportRowListFromLocaltors(String reportType, ReportMetaInfos aliasInfos) {
		List<ReportRow> rL = new ArrayList<>();
		List<ReportItemLocator> locL = new ArrayList<>();

		template.getRoot().forEach(new Visitor<ReportItemLocator>() {

			@Override
			public void visit(ReportItemLocator t) {
				if (true) {
					locL.add(t);
				}
			}
		}, false);

		for (ReportItemLocator ri : locL) {
			Integer idx = aliasInfos.getColumnIndexByAlias(reportType, ri.getKey());
			ReportRow rr = new ReportRow(years, ri.getKey(), ri);
			rr.columnIndex = idx;
			rL.add(rr);
		}
		return rL;
	}

	@Override
	public void execute(RestRequestContext rrc, ODatabaseSession dbs) throws IOException {

		DbReportMetaInfos aliasInfos = new DbReportMetaInfos();

		aliasInfos.initialize(dbs);
		final String reportType = DbUpgrader0_0_1.V_BALSHEET;
		String sql = "select from " + DbUpgrader0_0_1.V_BALSHEET + " where corpId=? ";

		DbUtil.executeQuery(dbs, sql, new Object[] { corpId }, new OResultSetHandler<Object>() {

			@Override
			public Object handle(OResultSet rst) {

				try {
					return doProcess(reportType, rst, aliasInfos, rrc.getReader(), rrc.getWriter());
				} catch (IOException e) {
					throw JcpsException.toRtException(e);
				}
			}
		});

	}

	private Object doProcess(String reportType, OResultSet rst, ReportMetaInfos aliasInfos, JsonReader reader,
			JsonWriter writer) throws IOException {
		// create a sorted empty ReportRow.
		List<ReportRow> rL = this.newReportRowListFromLocaltors(reportType, aliasInfos);

		// years
		int year = 0;
		while (rst.hasNext()) {
			OVertex v = rst.next().getVertex().get();

			for (int i = 0; i < rL.size(); i++) {

				ReportRow rr = rL.get(i);
				Double d = v.getProperty("d_" + rr.columnIndex);
				rr.set(year, d);
			}

			year++;
			if (year >= years) {
				// BUG here
				break;
			}
		}

		writer.beginArray();
		for (ReportRow rr : rL) {
			writer.beginObject();
			writer.name(rr.key);
			writer.beginArray();
			for (int i = 0; i < years; i++) {
				writer.value(rr.get(i));
			}

			writer.endArray();
			writer.endObject();
		}
		writer.endArray();
		return null;
	}

}
