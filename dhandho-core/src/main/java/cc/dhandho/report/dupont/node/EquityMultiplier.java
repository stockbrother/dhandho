package cc.dhandho.report.dupont.node;

import cc.dhandho.report.dupont.DupontAnalysis;

public class EquityMultiplier extends DividNode {

	public EquityMultiplier(DupontAnalysis tree) {
		super(tree);
		this.addChild(new AverageTotalAssetsNode(tree));
		this.addChild(new AverageEquityNode(tree));
	}
}
