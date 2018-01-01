package cc.dhandho.report.dupont;

public class ProfitMarginNode extends DividNode {
	public ProfitMarginNode(DupontAnalysis tree) {
		super(tree);
		this.addChild(new NetProfitNode(tree));
		this.addChild(new RevenueNode(tree));
	}

}
