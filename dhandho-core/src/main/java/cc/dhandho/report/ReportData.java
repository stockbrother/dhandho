package cc.dhandho.report;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.google.gson.stream.JsonWriter;

import cc.dhandho.RtException;

public class ReportData {
	public static class ReportRow {
		private String corpId;
		private Date reportDate;
		private Map<String, Double> valueMap = new HashMap<>();

		public ReportRow(String corpId2, Date reportDate2) {
			this.corpId = corpId2;
			this.reportDate = reportDate2;
		}

		public void set(String key, Double value) {
			this.valueMap.put(key, value);
		}

		public Map<String, Double> getValueMap() {
			return valueMap;
		}

		@Override
		public ReportRow clone() {
			ReportRow rt = new ReportRow(this.corpId, this.reportDate);
			rt.valueMap.putAll(this.valueMap);
			return rt;
		}

		public void dividBy(double d) {
			valueMap.entrySet().stream().forEach(new Consumer<Map.Entry<String, Double>>() {

				@Override
				public void accept(Entry<String, Double> t) {
					Double d1 = t.getValue();
					if (d1 == null) {
						return;
					}
					if (d1 == 0) {
						t.setValue(Double.NaN);
					} else {
						t.setValue(d / d1);
					}
				}
			});
		}

		public void multiple(double d) {
			valueMap.entrySet().stream().forEach(new Consumer<Map.Entry<String, Double>>() {

				@Override
				public void accept(Entry<String, Double> t) {
					Double d1 = t.getValue();
					if (d1 == null) {
						return;
					}
					t.setValue(d1 * d);
				}
			});
		}

		public void writerToJson(JsonWriter writer) {
			try {

				writer.beginObject();
				this.valueMap.entrySet().stream().forEach(new Consumer<Map.Entry<String, Double>>() {

					@Override
					public void accept(Entry<String, Double> t) {
						try {

							writer.name(t.getKey());
							Double value = t.getValue();
							if (Double.isNaN(value)) {
								writer.value((Double) null);
							} else {
								writer.value(value);
							}
						} catch (IOException e) {
							throw RtException.toRtException(e);
						}
					}
				});
				writer.endObject();
			} catch (IOException e) {
				throw RtException.toRtException(e);
			}
		}
	}

	private List<ReportRow> rowList = new ArrayList<>();

	public ReportRow addRow(String corpId, Date reportDate) {
		ReportRow rt = new ReportRow(corpId, reportDate);
		addRow(rt);
		return rt;
	}

	public ReportData addRow(ReportRow rr) {

		rowList.add(rr);
		return this;
	}

	@Override
	public ReportData clone() {
		ReportData rt = new ReportData();
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

}
