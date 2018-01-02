package cc.dhandho.report.dupont.node;

import cc.dhandho.report.dupont.DupontAnalysis;

public class RoeNode extends MultipleNode {

	public RoeNode(DupontAnalysis tree) {
		super(tree);
		this.addChild(new ProfitMarginNode(tree));
		this.addChild(new AssetTurnover(tree));
		this.addChild(new EquityMultiplier(tree));
	}

}
