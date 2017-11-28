package cc.dhandho.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.AliasInfos;
import cc.dhandho.DbAliasInfos;
import cc.dhandho.Quarter;
import cc.dhandho.RtException;
import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.GDBResultSetProcessor;
import cc.dhandho.report.ReportItemLocator;
import cc.dhandho.report.ReportItemLocators;
import cc.dhandho.util.DbInitUtil;
import cc.dhandho.util.Visitor;

public class ReportQueryJsonHandler extends AppContextAwareJsonHandler {

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

	private List<ReportRow> newReportRowListFromLocaltors(String reportType, AliasInfos aliasInfos) {
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
	public void execute(Gson gson, final JsonReader reader, JsonWriter writer) throws IOException {
		ODatabaseSession dbs = app.openDB();
		try {

			DbAliasInfos aliasInfos = new DbAliasInfos();

			aliasInfos.initialize(dbs);
			final String reportType = DbInitUtil.V_BALSHEET;
			String sql = "select from " + DbInitUtil.V_BALSHEET + " where corpId=? ";

			DbUtil.executeQuery(dbs, sql, new Object[] { corpId }, new GDBResultSetProcessor<Object>() {

				@Override
				public Object process(OResultSet rst) {

					try {
						return doProcess(reportType, rst, aliasInfos, reader, writer);
					} catch (IOException e) {
						throw RtException.toRtException(e);
					}
				}
			});
		} finally {
			dbs.close();
		}
	}

	private Object doProcess(String reportType, OResultSet rst, AliasInfos aliasInfos, JsonReader reader,
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
