package cc.dhandho.report.dupont;

import cc.dhandho.Metrics;

public class AverageTotalAssetsNode extends MetricNode {

	public AverageTotalAssetsNode(DupontAnalysis tree) {
		super(tree, Metrics.TOTAL_ASSETS);
	}

}
