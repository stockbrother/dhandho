package cc.dhandho.client;

public class ShowCommandHandler extends OptionDispatchingCommandHandler {
	public static final String OPT_m = "m";
	public static final String OPT_v = "v";
	public static final String OPT_f = "f";
	public static final String OPT_p = "p";
	public static final String OPT_c = "c";

	public ShowCommandHandler() {
		this.add(OPT_m, new ShowMetricDefinesCommandHandler());
		this.add(OPT_v, new ShowVariableCommandHandler());
		this.add(OPT_f, new ShowFileContentCommandHandler());
		this.add(OPT_p, new ShowPriceRatioCommandHandler());
		
	}
}
