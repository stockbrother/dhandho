package cc.dhandho.report.dupont;

public class MetricNode extends DefineNode {

	protected String metric;

	public MetricNode(DupontAnalysis tree, String metric) {
		super(tree);
		this.metric = metric;
	}

	@Override
	public ValueNode calculate(AnalysisContext ac) {
		Double value = this.tree.getReportEngine().getMetricValue(ac.getCorpId(), ac.getYear(), metric);
		return new ValueNode(this, value);
	}

}
