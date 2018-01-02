package cc.dhandho.report.dupont.node;

import cc.dhandho.Metrics;
import cc.dhandho.report.dupont.DupontAnalysis;

public class NetProfitNode extends MetricNode {

	public NetProfitNode(DupontAnalysis tree) {
		super(tree, Metrics.NET_PROFIT);
	}

}
