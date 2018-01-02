package cc.dhandho.report.dupont.node;

import cc.dhandho.report.dupont.DupontAnalysis;
import cc.dhandho.report.dupont.DupontAnalysis.Context;

public class DividNode extends DefineNode {

	public DividNode(DupontAnalysis tree) {
		super(tree);
	}

	@Override
	public ValueNode calculate(Context ac) {

		ValueNode rt = new ValueNode(this);

		double d = 0;
		boolean isFirst = true;
		boolean isNaN = false;

		for (DefineNode n : this.childList) {
			ValueNode vnode = n.calculate(ac);
			rt.addChild(vnode);

			Double dI = vnode.getValue();
			if (dI == null || dI.isNaN()) {
				isNaN = true;
				break;
			}
			if (isFirst) {
				isFirst = false;
				d = dI;
			} else {
				d /= dI;
			}
		}

		rt.setValue(isNaN ? null : d);

		return rt;

	}

}
