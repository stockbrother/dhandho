package cc.dhandho.report.dupont.node;

import cc.dhandho.Metrics;
import cc.dhandho.report.dupont.DupontAnalysis;

public class AverageTotalAssetsNode extends MetricNode {

	public AverageTotalAssetsNode(DupontAnalysis tree) {
		super(tree, Metrics.TOTAL_ASSETS);
	}

}
