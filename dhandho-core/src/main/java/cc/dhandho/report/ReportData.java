package cc.dhandho.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportData {
	public static class ReportRow {
		private String corpId;
		private Date reportDate;
		private Map<String, Double> valueMap = new HashMap<>();

		public void set(String key, Double value) {
			this.valueMap.put(key, value);
		}

		public Map<String, Double> getValueMap() {
			return valueMap;
		}

	}

	private List<ReportRow> rowList = new ArrayList<>();

	public ReportRow addRow() {
		ReportRow rt = new ReportRow();
		rowList.add(rt);
		return rt;
	}

	public StringBuffer print(StringBuffer sb) {
		sb.append(rowList);
		return sb;
	}

}
