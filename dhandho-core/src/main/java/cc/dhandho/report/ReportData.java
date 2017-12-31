package cc.dhandho.report;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.RtException;
import cc.dhandho.input.xueqiu.DateUtil;
import cc.dhandho.util.JsonUtil;

public class ReportData {
	public static class ReportRow {
		private String corpId;
		private Date reportDate;
		private Double[] valueArray;

		public ReportRow(String corpId2, Date reportDate2, Double[] values) {
			this.corpId = corpId2;
			this.reportDate = reportDate2;
			this.valueArray = values;
		}

		public Double[] getValueArray() {
			return valueArray;
		}

		public void set(int idx, Double value) {
			this.valueArray[idx] = value;
		}

		@Override
		public ReportRow clone() {
			Double[] dA = Arrays.copyOf(valueArray, valueArray.length);

			ReportRow rt = new ReportRow(this.corpId, this.reportDate, dA);
			return rt;
		}

		public void dividBy(double d) {

			for (int i = 0; i < this.valueArray.length; i++) {
				Double d1 = valueArray[i];

				if (d1 != null) {

					if (d1 == 0) {
						valueArray[i] = null;
					} else {
						valueArray[i] = d / d1;
					}
				}
			}
		}

		public void multiple(double d) {
			for (int i = 0; i < this.valueArray.length; i++) {
				Double d1 = valueArray[i];

				if (d1 != null) {
					valueArray[i] = d * d1;
				}
			}
		}

		public void writerToJson(JsonWriter writer) {
			try {

				writer.beginArray();
				writer.value(this.corpId);
				writer.value(DateUtil.format(this.reportDate));
				for (int i = 0; i < this.valueArray.length; i++) {
					Double d1 = valueArray[i];
					d1 = (d1 == null || d1.isNaN()) ? null : d1;
					writer.value(d1);
				}
				writer.endArray();
			} catch (IOException e) {
				throw RtException.toRtException(e);
			}
		}
	}

	private String[] headerArray;

	private List<ReportRow> rowList = new ArrayList<>();

	public ReportData(String[] headerArray) {
		this.headerArray = headerArray;
	}

	public ReportRow addRow(String corpId, Date reportDate, Double[] valueArray) {
		ReportRow rt = new ReportRow(corpId, reportDate, valueArray);
		addRow(rt);
		return rt;
	}

	public ReportData addRow(ReportRow rr) {

		rowList.add(rr);
		return this;
	}

	@Override
	public ReportData clone() {
		ReportData rt = new ReportData(this.headerArray);
		this.rowList.stream().forEach(new Consumer<ReportRow>() {

			@Override
			public void accept(ReportRow t) {
				rt.addRow(t.clone());
			}
		});

		return rt;
	}

	public ReportData dividBy(double d) {
		this.rowList.stream().forEach(new Consumer<ReportRow>() {

			@Override
			public void accept(ReportRow t) {
				t.dividBy(d);
			}
		});
		return this;
	}

	public ReportData divid(double d) {
		return this.multiple(1D / d);
	}

	public ReportData multiple(double d) {
		this.rowList.stream().forEach(new Consumer<ReportRow>() {

			@Override
			public void accept(ReportRow t) {
				t.multiple(d);
			}
		});
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
			throw RtException.toRtException(e);
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
					t.writerToJson(writer);
				}
			});
			writer.endArray();
			writer.endObject();
		} catch (IOException e) {
			throw new RtException(e);
		}

	}

	public static ReportData parseJson(String string) {
		//
		JsonObject json = (JsonObject) JsonUtil.parse(string);
		JsonArray headerA = (JsonArray) json.get("headerArray");
		String[] header = new String[headerA.size()];
		for (int i = 0; i < headerA.size(); i++) {
			header[i] = headerA.get(i).getAsString();
		}

		JsonArray arry = (JsonArray) json.get("rowArray");
		ReportData rt = new ReportData(header);

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

}
