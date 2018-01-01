package cc.dhandho.report.dupont;

import cc.dhandho.report.ReportEngine;

public class DupontAnalysis {
	private ReportEngine reportEngine;
	private RoeNode roeNode;

	public DupontAnalysis(ReportEngine re) {
		this.reportEngine = re;
		this.roeNode = new RoeNode(this);
	}

	public ReportEngine getReportEngine() {
		return reportEngine;
	}

	public AnalysisContext execute(AnalysisContext ac) {
		ValueNode vNode = roeNode.calculate(ac);
		ac.setValueNode(vNode);
		return ac;
	}
}
