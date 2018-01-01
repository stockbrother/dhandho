package cc.dhandho.report.dupont;

public class MultipleNode extends DefineNode {

	public MultipleNode(DupontAnalysis tree) {
		super(tree);
	}

	@Override
	public ValueNode calculate(AnalysisContext ac) {

		ValueNode rt = new ValueNode(this);

		double d = 1;
		boolean isNaN = false;

		for (DefineNode n : this.childList) {
			ValueNode vnode = n.calculate(ac);
			rt.addChild(vnode);
			
			Double dI = vnode.getValue();
			if (dI == null || dI.isNaN()) {
				isNaN = true;
				break;
			}
			d *= dI;
		}

		rt.setValue(isNaN ? null : d);

		return rt;

	}

}
