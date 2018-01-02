package cc.dhandho.report.dupont.node;

import cc.dhandho.report.dupont.DupontAnalysis;
import cc.dhandho.report.dupont.DupontAnalysis.Context;

public class MetricNode extends DefineNode {

	protected String metric;

	public MetricNode(DupontAnalysis tree, String metric) {
		super(tree);
		this.metric = metric;
	}

	@Override
	public ValueNode calculate(Context ac) {
		Double value = this.tree.getReportEngine().getMetricValue(ac.getCorpId(), ac.getYear(), metric);
		return new ValueNode(this, value);
	}

}
