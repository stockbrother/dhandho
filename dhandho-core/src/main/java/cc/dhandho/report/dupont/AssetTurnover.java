package cc.dhandho.report.dupont;

public class AssetTurnover extends DividNode {

	public AssetTurnover(DupontAnalysis tree) {
		super(tree);
		this.addChild(new RevenueNode(tree));
		this.addChild(new AverageTotalAssetsNode(tree));

	}

}
