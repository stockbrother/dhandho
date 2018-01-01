package cc.dhandho.report.dupont;

public class AnalysisContext {

	private String corpId;

	private int year;

	private ValueNode valueNode;

	public AnalysisContext(String corpId, int year) {
		this.corpId = corpId;
		this.year = year;
	}

	public ValueNode getValueNode() {
		return valueNode;
	}

	public String getCorpId() {
		return corpId;
	}

	public int getYear() {
		return year;
	}

	public void setValueNode(ValueNode vNode) {
		this.valueNode = vNode;
	}
}
