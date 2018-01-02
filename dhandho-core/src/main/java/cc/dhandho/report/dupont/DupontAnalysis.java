package cc.dhandho.report.dupont;

import java.util.Iterator;

import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.dupont.node.RoeNode;
import cc.dhandho.report.dupont.node.ValueNode;

public class DupontAnalysis {
	public static class Context {

		private String corpId;

		private int year;

		// The root node of the result tree.
		private ValueNode valueNode;

		public Context(String corpId, int year) {
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

	private ReportEngine reportEngine;
	
	private RoeNode roeNode;

	public DupontAnalysis(ReportEngine re) {
		this.reportEngine = re;
		this.roeNode = new RoeNode(this);
	}

	public ReportEngine getReportEngine() {
		return reportEngine;
	}

	public Context execute(String corpId, int year) {
		return execute(new Context(corpId, year));
	}

	public Context execute(Context ac) {
		ValueNode vNode = roeNode.calculate(ac);
		ac.setValueNode(vNode);
		return ac;
	}

	public void execute(Iterator<String> corpIdIt, int year) {
		while (corpIdIt.hasNext()) {
			String corpId = corpIdIt.next();
			Context ac = execute(corpId, year);
			
		}
	}

}
