package cc.dhandho.report.dupont.node;

import cc.dhandho.report.dupont.DupontAnalysis;

public class AssetTurnover extends DividNode {

	public AssetTurnover(DupontAnalysis tree) {
		super(tree);
		this.addChild(new RevenueNode(tree));
		this.addChild(new AverageTotalAssetsNode(tree));

	}

}
