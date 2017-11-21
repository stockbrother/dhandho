package cc.dhandho.gwt.client.app.control;

import java.util.Stack;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import cc.dhandho.gwt.client.app.InitDataJson;
import cc.dhandho.gwt.client.app.InitDataJson.TableRowJson;
import cc.dhandho.gwt.client.app.view.MainPanel;

public class RequestEditor {

	private String group;

	private MainPanel mainPanel;

	public RequestEditor(MainPanel mainPanel) {

		this.mainPanel = mainPanel;
	}

	private JSONArray buildDates() {
		JSONArray rt = new JSONArray();
		for (int i = 0; i < 5; i++) {
			rt.set(i, new JSONNumber(2016 - i));
		}

		return rt;
	}

	public JSONObject buildRequest() {
		JSONObject rt = new JSONObject();

		String corpId = mainPanel.corpId.getText();
		rt.put("corpId", new JSONString(corpId));
		JSONArray dates = buildDates();
		rt.put("dates", dates);

		JSONArray metrics = new JSONArray();
		TableRowJson group = this.mainPanel.mdTable.metricDefineGroupTable.getMetricGroupDefine(this.group);

		if (group != null) {
			int first = 3;
			
			for (int i = first; i < group.size(); i++) {
				String m = group.get(i).isString().stringValue();
				JSONValue mJson = buildRequestForMetric(m, new Stack<String>());
				metrics.set((i - first), mJson);
			}
		}
		rt.put("metrics", metrics);

		return rt;
	}

	private JSONValue buildRequestForMetric(String name, Stack<String> processing) {
		TableRowJson metric = this.mainPanel.mdTable.metricDefineTable.getMetricDefine(name);

		if (metric == null) {// is leaf metric.
			return new JSONString(name);
		} else {
			if (processing.contains(name)) {
				throw new RuntimeException("looping dependency:" + name);
			}
			processing.push(name);
			JSONObject obj = new JSONObject();

			String operator = metric.get("Operator").isString().stringValue();
			int offset = (int) metric.get("Offset").isNumber().doubleValue();

			JSONArray metricArray = new JSONArray();

			for (int i = 0; i < 100; i++) {

				JSONValue metricJ = metric.get("Metric" + (i + 1));
				if (metricJ == null) {
					break;
				}
				String metricS = metricJ.isString().stringValue();
				JSONValue m1 = buildRequestForMetric(metricS, processing);

				metricArray.set(i, m1);
			}

			obj.put("name", new JSONString(name));
			obj.put("offset", new JSONNumber(offset));
			obj.put("operator", new JSONString(operator));
			obj.put("metrics", metricArray);
			processing.pop();
			return obj;
		}

	}

	public void setGroup(String group) {
		this.group = group;
		this.buildRequest();
	}
}
