package cc.dhandho.client;

public class ShowCommandHandler extends OptionDispatchingCommandHandler {
	public static final String OPT_m = "m";
	public static final String OPT_v = "v";

	public ShowCommandHandler() {
		this.add(OPT_m, new ShowMetricDefinesCommandHandler());
		this.add(OPT_v, new ShowVariableCommandHandler());
		
	}
}
