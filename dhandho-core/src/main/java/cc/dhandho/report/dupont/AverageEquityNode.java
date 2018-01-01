package cc.dhandho.report.dupont;

import cc.dhandho.Metrics;

public class AverageEquityNode extends MetricNode {

	public AverageEquityNode(DupontAnalysis tree) {
		super(tree, Metrics.EQUITY);
	}

}
