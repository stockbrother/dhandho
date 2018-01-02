package cc.dhandho.report.dupont.node;

import cc.dhandho.Metrics;
import cc.dhandho.report.dupont.DupontAnalysis;

public class RevenueNode extends MetricNode {

	public RevenueNode(DupontAnalysis tree) {
		super(tree, Metrics.REVENUE);
	}

}
