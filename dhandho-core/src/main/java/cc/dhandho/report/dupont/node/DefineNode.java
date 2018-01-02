package cc.dhandho.report.dupont.node;

import java.util.ArrayList;
import java.util.List;

import cc.dhandho.report.dupont.DupontAnalysis;
import cc.dhandho.report.dupont.DupontAnalysis.Context;

public abstract class DefineNode {

	protected List<DefineNode> childList = new ArrayList<DefineNode>();

	protected DupontAnalysis tree;

	public DefineNode(DupontAnalysis tree) {
		this.tree = tree;
	}

	public void addChild(DefineNode child) {
		this.childList.add(child);
	}

	public abstract ValueNode calculate(Context ac);

}
