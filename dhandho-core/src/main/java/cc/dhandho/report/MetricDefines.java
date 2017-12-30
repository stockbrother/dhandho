package cc.dhandho.report;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.apache.commons.vfs2.FileObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.RtException;
import cc.dhandho.util.JsonUtil;

/**
 * Load define of metrics from formated xml as below: <code>
 
    <metrics for="xueqiu">

	    <group name="COMMON">
		    <metric name="权益乘数" op="/">
			    <metric ref="资产总计" />
			    <metric ref="所有者权益_或股东权益_合计" />
		    </metric>
	    </group>

	    <group name="NJR">
	        ...
	    </group>
	    <group name="JR">
	        ...
	    </group>
	    
     <metrics>
 
   </code>
 * 
 * @author wu
 *
 */
public class MetricDefines {

	public static enum Group {
		COMMON, JR, NJR
	}

	public static enum Operator {
		PLUS, MINUS, MULTIPLE, DIV;

		private static Map<Operator, String> operatorSignMap = new HashMap<>();
		static {

			operatorSignMap.put(Operator.PLUS, "+");
			operatorSignMap.put(Operator.MINUS, "-");
			operatorSignMap.put(Operator.MULTIPLE, "*");
			operatorSignMap.put(Operator.DIV, "/");

		}

		public String getSign() {
			return operatorSignMap.get(this);
		}

	}

	public static class MetricDefine {
		private static AtomicInteger nextId = new AtomicInteger();
		private int id;
		private Group group;
		private String name;
		private Operator operator;
		private List<String> childMetricList = new ArrayList<>();

		public MetricDefine() {
			id = nextId.incrementAndGet();
		}

		public int getId() {
			return id;
		}

		public Group getGroup() {
			return group;
		}

		public String getName() {
			return name;
		}

		public Operator getOperator() {
			return operator;
		}

		public List<String> getChildMetricList() {
			return childMetricList;
		}

	}

	public static MetricDefines load(FileObject fo) throws IOException {
		return load(fo.getContent().getInputStream());
	}

	public static MetricDefines load(InputStream is) throws IOException {

		SAXReader sreader = new SAXReader();
		try {
			Document doc = sreader.read(is);
			Element ele = doc.getRootElement();
			MetricDefines rt = new MetricDefines(ele);
			rt.init();
			return rt;
		} catch (DocumentException e) {
			throw new IOException("", e);
		}

	}

	private static Map<String, Operator> operatorMap = new HashMap<>();

	static {
		operatorMap.put("+", Operator.PLUS);
		operatorMap.put("-", Operator.MINUS);
		operatorMap.put("*", Operator.MULTIPLE);
		operatorMap.put("/", Operator.DIV);

	}
	private Element root;

	private Map<String, MetricDefine> metricNameMap;

	private Map<Integer, MetricDefine> metricIdMap;

	private MetricDefines(Element ele) {
		this.root = ele;
	}

	public Stream<MetricDefine> getMetricDefineStream() {
		return metricNameMap.values().stream();
	}

	private void init() {
		this.metricNameMap = new HashMap<>();
		this.metricIdMap = new HashMap<>();
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

		MetricDefine m = new MetricDefine();
		m.group = group;
		m.name = ele.attributeValue("name");
		m.operator = operatorMap.get(opS);
		for (Iterator<Element> it = ele.elementIterator(); it.hasNext();) {
			Element child = it.next();
			String ref = child.attributeValue("ref");
			m.childMetricList.add(ref);
		}

		MetricDefine old = this.metricNameMap.put(m.name, m);
		if (old != null) {
			throw new RtException("duplicated metric:" + m.name);
		}
		this.metricIdMap.put(m.id, m);
	}

	public JsonObject buildMetricRequestAsJson(String corpId, int[] years, String[] metrics) {
		StringWriter sWriter = new StringWriter();
		this.buildMetricRequestAsJson(corpId, years, metrics, new JsonWriter(sWriter));
		return (JsonObject) JsonUtil.parse(sWriter.getBuffer().toString());
	}

	public void buildMetricRequestAsJson(String corpId, int[] years, String[] metrics, JsonWriter writer) {
		try {

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
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}
	}

	public MetricDefine getMetric(int id) {
		return this.metricIdMap.get(id);
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
		MetricDefine m = this.metricNameMap.get(metric);

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
		writer.value(m.operator.getSign());
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
