package cc.dhandho.gwt.client.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class InitDataJson {
	
	
	
	public static class TableRowJson {
		TableHeaderJson header;
		JSONArray row;
		TableJson table;

		TableRowJson(TableHeaderJson header, JSONArray row, TableJson table) {
			this.header = header;
			this.row = row;
			this.table = table;
		}

		public int size() {
			return row.size();
		}

		public JSONValue get(String key) {
			Integer idx = this.header.getIndex(key);

			if (idx == null) {
				return null;
			}

			return get(idx);
		}

		public JSONValue get(int idx) {
			if (idx > this.row.size() - 1) {
				return null;
			}
			return this.row.get(idx);
		}

	}

	public static class TableHeaderJson {
		JSONArray array;
		Map<String, Integer> map_cache;

		public static TableHeaderJson valueOf(JSONArray array) {
			TableHeaderJson rt = new TableHeaderJson();
			rt.array = array;
			rt.map_cache = new HashMap<>();
			for (int i = 0; i < array.size(); i++) {
				String key = array.get(i).isString().stringValue();
				rt.map_cache.put(key, i);
			}
			return rt;
		}

		public Integer getIndex(String key) {
			return map_cache.get(key);
		}

	}

	public static class TableJson {

		JSONArray body;

		TableHeaderJson header;

		List<TableRowJson> list_cache;

		TableJson(JSONObject json) {
			list_cache = new ArrayList<>();
			JSONValue headerJ = json.get("header");
			if (headerJ != null) {
				header = TableHeaderJson.valueOf(headerJ.isArray());
			}

			body = json.get("body").isArray();

			for (int i = 0; i < body.size(); i++) {
				JSONArray row = body.get(i).isArray();
				TableRowJson m = new TableRowJson(header, row, this);

				list_cache.add(m);
			}
		}

		public List<TableRowJson> getRowList() {
			return this.list_cache;
		}

	}

	public static class MetricDefineTableJson extends TableJson {

		private Map<String, TableRowJson> map_cache;

		public MetricDefineTableJson(JSONObject json) {
			super(json);

			map_cache = new HashMap<>();
			for (int i = 0; i < list_cache.size(); i++) {
				TableRowJson row = this.list_cache.get(i);
				String name = row.get("Name").isString().stringValue();
				TableRowJson old = map_cache.put(name, row);
				if (old != null) {
					throw new RuntimeException("duplicated:" + name);
				}
			}

		}

		public TableRowJson getMetricDefine(String name) {
			return this.map_cache.get(name);
		}

	}

	public static class MetricDefineGroupTableJson extends TableJson {
		public static int FIRST = 2;
		public static int SECOND = FIRST + 1;
		
		private Map<String, TableRowJson> map_cache;

		public MetricDefineGroupTableJson(JSONObject json) {
			super(json);

			map_cache = new HashMap<>();
			for (int i = 0; i < list_cache.size(); i++) {
				TableRowJson row = this.list_cache.get(i);
				String name = row.get(FIRST).isString().stringValue();

				TableRowJson old = map_cache.put(name, row);
				if (old != null) {
					throw new RuntimeException("duplicated:" + name);
				}
			}

		}

		public TableRowJson getMetricGroupDefine(String name) {
			return this.map_cache.get(name);
		}

	}

	public MetricDefineTableJson metricDefineTable;
	public MetricDefineGroupTableJson metricDefineGroupTable;

	public static InitDataJson valueOf(JSONObject json) {
		InitDataJson rt = new InitDataJson();
		JSONObject table1 = json.get("metric-define-table").isObject();
		rt.metricDefineTable = new MetricDefineTableJson(table1);
		JSONObject table2 = json.get("metric-define-group-table").isObject();
		rt.metricDefineGroupTable = new MetricDefineGroupTableJson(table2);
		return rt;
	}

}