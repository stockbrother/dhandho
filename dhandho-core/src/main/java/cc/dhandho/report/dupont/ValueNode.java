package cc.dhandho.report.dupont;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonWriter;

import cc.dhandho.RtException;
import cc.dhandho.util.JsonUtil;

public class ValueNode {
	protected DefineNode define;

	protected Double value;

	protected List<ValueNode> childList = new ArrayList<>();

	public ValueNode(DefineNode define) {
		this(define, null);
	}

	public ValueNode(DefineNode define, Double d) {
		this.define = define;
		this.value = d;
	}

	public DefineNode getDefine() {
		return define;
	}

	public List<ValueNode> getChildList() {
		return childList;
	}

	public void setValue(Double d) {
		this.value = d;
	}

	public Double getValue() {
		return value;
	}

	public void addChild(ValueNode child) {
		this.childList.add(child);
	}

	public StringBuilder toJson(StringBuilder sb) {
		JsonWriter writer = JsonUtil.newJsonWriter(sb);
		toJson(writer);
		return sb;
	}

	public void toJson(JsonWriter writer) {

		try {
			writer.beginObject();
			writer.name("value").value(value);
			writer.name("childList");
			writer.beginArray();
			for (ValueNode vNode : this.childList) {
				vNode.toJson(writer);
			}
			writer.endArray();
			writer.endObject();
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}

	}

}
