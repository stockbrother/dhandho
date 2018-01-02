package cc.dhandho.report.dupont.node;

import cc.dhandho.report.dupont.DupontAnalysis;

public class ProfitMarginNode extends DividNode {
	public ProfitMarginNode(DupontAnalysis tree) {
		super(tree);
		this.addChild(new NetProfitNode(tree));
		this.addChild(new RevenueNode(tree));
	}

}
