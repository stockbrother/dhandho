package cc.dhandho.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.gson.stream.JsonWriter;

import cc.dhandho.RtException;

public class MetricsDefine {

	public static enum Group {
		COMMON, JR, NJR
	}

	public static enum Operator {
		PLUS, MINUS, MULTIPLE, DIV
	}

	public static class Metric {
		private Group group;
		private String name;
		private Operator operator;
		private List<String> childMetricList = new ArrayList<>();

	}

	public static MetricsDefine load(InputStream is) throws IOException {

		SAXReader sreader = new SAXReader();
		try {
			Document doc = sreader.read(is);
			Element ele = doc.getRootElement();
			MetricsDefine rt = new MetricsDefine(ele);
			rt.init();
			return rt;
		} catch (DocumentException e) {
			throw new IOException("", e);
		}

	}

	private static Map<String, Operator> operatorMap = new HashMap<>();

	private static Map<Operator, String> operatorMap2 = new HashMap<>();

	static {
		operatorMap.put("+", Operator.PLUS);
		operatorMap.put("-", Operator.MINUS);
		operatorMap.put("*", Operator.MULTIPLE);
		operatorMap.put("/", Operator.DIV);

		operatorMap2.put(Operator.PLUS, "+");
		operatorMap2.put(Operator.MINUS, "-");
		operatorMap2.put(Operator.MULTIPLE, "*");
		operatorMap2.put(Operator.DIV, "/");

	}
	private Element root;

	private Map<String, Metric> metricMap;

	public MetricsDefine(Element ele) {
		this.root = ele;
	}

	private void init() {
		this.metricMap = new HashMap<>();
		for (Iterator<Element> it = root.elementIterator(); it.hasNext();) {
			Element child = it.next();
			processGroup(child);
		}

	}

	private void processGroup(Element ele) {
		String name = ele.attributeValue("name");
		Group group = Group.valueOf(name);

		for (Iterator<Element> it = ele.elementIterator(); it.hasNext();) {
			Element child = it.next();
			processMetric(group, child);
		}

	}

	private void processMetric(Group group, Element ele) {
		String opS = ele.attributeValue("op");

		Metric m = new Metric();
		m.group = group;
		m.name = ele.attributeValue("name");
		m.operator = operatorMap.get(opS);
		for (Iterator<Element> it = ele.elementIterator(); it.hasNext();) {
			Element child = it.next();
			String ref = child.attributeValue("ref");
			m.childMetricList.add(ref);
		}

		Metric old = this.metricMap.put(m.name, m);
		if (old != null) {
			throw new RtException("duplicated metric:" + m.name);
		}
	}

	public void buildMetricRequestAsJson(String corpId, int[] years, String[] metrics, JsonWriter writer)
			throws IOException {
		writer.beginObject();
		writer.name("corpId");
		writer.value(corpId);
		writer.name("dates");
		writer.beginArray();
		for (int y : years) {
			writer.value(y);
		}
		writer.endArray();
		writer.name("metrics");
		buildMetricArrayAsJson(metrics, writer);
		writer.endObject();
	}

	public void buildMetricArrayAsJson(String[] metrics, JsonWriter writer) throws IOException {
		writer.beginArray();
		for (String metric : metrics) {
			buildMetricAsJson(metric, writer);
		}
		writer.endArray();
	}

	public void buildMetricAsJson(String metric, JsonWriter writer) throws IOException {
		doBuildMetricAsJson(metric, writer, new Stack<String>());
	}

	private void doBuildMetricAsJson(String metric, JsonWriter writer, Stack<String> stack) throws IOException {
		Metric m = this.metricMap.get(metric);

		if (m == null) {// leaf
			writer.value(metric);
			return;
		}
		// non-leaf metric,composite metric.
		if (stack.contains(metric)) {
			throw new RtException("loop dependence, stack:" + stack);
		}
		stack.push(metric);
		writer.beginObject();
		writer.name("name");
		writer.value(metric);
		writer.name("offset");
		writer.value(0);
		writer.name("operator");
		writer.value(operatorMap2.get(m.operator));
		writer.name("metrics");
		writer.beginArray();
		for (String key : m.childMetricList) {
			doBuildMetricAsJson(key, writer, stack);
		}

		writer.endArray();
		writer.endObject();
		stack.pop();
	}

}
