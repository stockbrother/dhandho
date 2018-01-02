package cc.dhandho.report.dupont.node;

import cc.dhandho.Metrics;
import cc.dhandho.report.dupont.DupontAnalysis;

public class AverageEquityNode extends MetricNode {

	public AverageEquityNode(DupontAnalysis tree) {
		super(tree, Metrics.EQUITY);
	}

}
