package cc.dhandho.report;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.age5k.jcps.JcpsException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.input.xueqiu.DateUtil;
import cc.dhandho.report.chart.SvgChartWriter;
import cc.dhandho.util.JsonUtil;

public class CorpDatedMetricReportData extends DoubleTable {

	public static class ReportRow {

		private CorpDatedMetricReportData table;
		private String corpId;
		private Date reportDate;
		int row;

		public ReportRow(CorpDatedMetricReportData table, String corpId2, Date reportDate2, int row) {
			this.table = table;
			this.corpId = corpId2;
			this.reportDate = reportDate2;
			this.row = row;
		}

		public String getCorpId() {
			return corpId;
		}

		public Date getReportDate() {
			return reportDate;
		}

		public StringBuilder toHtml(StringBuilder sb) {
			return sb;
		}

	}

	private String[] headerArray;

	public String[] getHeaderArray() {
		return headerArray;
	}

	private List<ReportRow> rowList = new ArrayList<>();

	public CorpDatedMetricReportData(String[] headerArray) {
		super();
		this.headerArray = headerArray;
	}

	public ReportRow addRow(String corpId, Date reportDate, Double[] valueArray) {
		int row = super.addRow(valueArray);
		ReportRow rr = new ReportRow(this, corpId, reportDate, row);
		rowList.add(rr);
		return rr;
	}

	@Override
	public CorpDatedMetricReportData clone() {

		CorpDatedMetricReportData rt = new CorpDatedMetricReportData(this.headerArray);

		this.rowList.stream().forEach(new Consumer<ReportRow>() {

			@Override
			public void accept(ReportRow t) {
				Double[] valueArray = CorpDatedMetricReportData.this.cloneRow(t.row);
				rt.addRow(t.corpId, t.reportDate, valueArray);
			}
		});

		return rt;
	}

	public CorpDatedMetricReportData dividBy(double d) {
		super.dividBy(d);
		return this;
	}

	public CorpDatedMetricReportData divid(double d) {
		return this.multiple(1D / d);
	}

	public CorpDatedMetricReportData multiple(double d) {
		super.multiple(d);
		return this;
	}

	public List<ReportRow> getRowList() {
		return this.rowList;
	}

	public Stream<ReportRow> rowStream() {
		return this.rowList.stream();
	}

	public StringBuffer print(StringBuffer sb) {
		sb.append(rowList);
		return sb;
	}

	public JsonObject toJson() {
		StringBuilder sb = new StringBuilder();
		writeToJson(sb);
		return (JsonObject) JsonUtil.parse(sb.toString());
	}

	public void writeToJson(Appendable sb) {
		StringWriter sWriter = new StringWriter();
		writeToJson(new JsonWriter(sWriter));
		try {
			sb.append(sWriter.getBuffer());
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public void writeToJson(JsonWriter writer) {
		try {

			writer.beginObject();
			writer.name("headerArray");
			writer.beginArray();

			for (int i = 0; i < this.headerArray.length; i++) {
				writer.value(this.headerArray[i]);
			}

			writer.endArray();

			writer.name("rowArray");
			writer.beginArray();
			this.rowList.stream().forEach(new Consumer<ReportRow>() {

				@Override
				public void accept(ReportRow t) {
					writeRowToJson(t, writer);
				}
			});
			writer.endArray();
			writer.endObject();
		} catch (IOException e) {
			throw new JcpsException(e);
		}

	}

	public void writeRowToJson(ReportRow rr, JsonWriter writer) {
		try {

			writer.beginArray();
			writer.value(rr.corpId);
			writer.value(DateUtil.format(rr.reportDate));
			Double[] valueArray = super.getRow(rr.row);

			for (int i = 0; i < valueArray.length; i++) {
				Double d1 = valueArray[i];
				d1 = (d1 == null || d1.isNaN()) ? null : d1;
				writer.value(d1);
			}
			writer.endArray();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public static CorpDatedMetricReportData parseJson(String string) {
		JsonObject json = (JsonObject) JsonUtil.parse(string);
		return parseJson(json);
	}

	public static CorpDatedMetricReportData parseJson(JsonObject json) {
		//
		JsonArray headerA = (JsonArray) json.get("headerArray");
		String[] header = new String[headerA.size()];
		for (int i = 0; i < headerA.size(); i++) {
			header[i] = headerA.get(i).getAsString();
		}

		JsonArray arry = (JsonArray) json.get("rowArray");
		CorpDatedMetricReportData rt = new CorpDatedMetricReportData(header);

		for (int i = 0; i < arry.size(); i++) {
			JsonArray arry2 = arry.get(i).getAsJsonArray();
			String corpId = arry2.get(0).getAsString();
			String dateS = arry2.get(1).getAsString();
			Date reportDate = DateUtil.parse(dateS);

			Double[] valueArray = new Double[headerA.size()];
			for (int j = 2; j < arry2.size(); j++) {
				JsonElement jJ = arry2.get(j);
				valueArray[j - 2] = jJ.isJsonNull() ? null : jJ.getAsDouble();
			}
			rt.addRow(corpId, reportDate, valueArray);
		}

		return rt;
	}

	public StringBuilder toSvgDiv(StringBuilder sb) {
		sb.append("<div style='width:600;height:320;'>");
		toSvg(sb);
		sb.append("</div>");
		return sb;
	}

	public StringBuilder toSvg(StringBuilder sb) {

		new SvgChartWriter().writeSvg(this, sb);

		return sb;
	}

	public StringBuilder toSvgDivAndHtmlTable(StringBuilder sb) {
		sb.append("<table>");
		sb.append("<tr>").append("<td>");
		toSvgDiv(sb);
		sb.append("</td>").append("</tr>");

		sb.append("<tr>").append("<td>");
		toHtmlTable(sb);
		sb.append("</td>").append("</tr>");

		sb.append("<table>");
		return sb;
	}

	public StringBuilder toHtmlTable(StringBuilder sb) {
		sb.append("<table>");
		sb.append("<thead>");

		sb.append("<tr>");
		sb.append("<td>");
		sb.append("CorpID");
		sb.append("</td>");
		sb.append("<td>");
		sb.append("ReportDate");
		sb.append("</td>");
		for (int i = 0; i < this.headerArray.length; i++) {
			sb.append("<td>");
			sb.append(this.headerArray[i]);
			sb.append("</td>");
		}

		sb.append("</tr>");

		sb.append("</thead>");
		sb.append("<tbody>");
		this.rowList.stream().forEach(new Consumer<ReportRow>() {

			@Override
			public void accept(ReportRow t) {

				sb.append("<tr>");

				sb.append("<td>");
				sb.append(t.corpId);
				sb.append("</td>");
				sb.append("<td>");
				sb.append(DateUtil.format(t.reportDate));
				sb.append("</td>");
				Double[] valueArray = CorpDatedMetricReportData.this.getRow(t.row);
				for (int i = 0; i < valueArray.length; i++) {

					sb.append("<td>");
					if (valueArray[i] != null) {
						sb.append(valueArray[i]);
					} else {
						sb.append("NaN");
					}

					sb.append("</td>");
				}

				sb.append("</tr>");

			}
		});
		sb.append("</tbody>");
		sb.append("</table>");
		return sb;
	}

}
