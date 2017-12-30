package cc.dhandho.client;

public class ShowCommandHandler extends OptionDispatchingCommandHandler {
	
	public static final String OPT_M = "M";//MetricDefines.	
	public static final String OPT_v = "v";
	public static final String OPT_r = "r";
	public static final String OPT_c = "c";
	public static final String OPT_m = "m";

	public ShowCommandHandler() {
		this.add(OPT_M, new ShowMetricDefinesCommandHandler());
		this.add(OPT_v, new ShowVariableCommandHandler());		
		this.add(OPT_r, new ShowReportCommandHandler());
		
	}
}
