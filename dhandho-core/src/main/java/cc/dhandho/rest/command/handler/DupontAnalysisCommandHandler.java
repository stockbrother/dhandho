package cc.dhandho.rest.command.handler;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.google.gson.JsonObject;

import cc.dhandho.rest.command.CommandContext;
import cc.dhandho.rest.handler.DupontAnalysisJsonHandler;
import cc.dhandho.rest.handler.DupontReportJsonHandler;
import cc.dhandho.rest.handler.DupontSvgJsonHandler;

public class DupontAnalysisCommandHandler extends RestRequestCommandHandler {

	public static final String OPT_a = "a";
	public static final String OPT_r = "r";
	public static final String OPT_s = "s";
	public static final String OPT_c = "c";
	public static final String OPT_y = "y";
	public static final String OPT_f = "f";
	static NumberFormat formatter = new DecimalFormat("#0.00");

	@Override
	protected String buildRequest(CommandContext cc, JsonObject req) {
		if (cc.getCommandLine().hasOption(OPT_a)) {
			String yearS = cc.getCommandLine().getOptionValue(OPT_y);
			int year = yearS == null ? 2016 : Integer.parseInt(yearS);

			req.addProperty("year", year);
			return DupontAnalysisJsonHandler.class.getName();

		} else if (cc.getCommandLine().hasOption(OPT_r)) {

			String code = cc.getCommandLine().getOptionValue(OPT_c);
			String yearS = cc.getCommandLine().getOptionValue(OPT_y);
			int year = yearS == null ? 2016 : Integer.parseInt(yearS);

			req.addProperty("corpId", code);
			req.addProperty("year", year);

			// filter
			String filterS = cc.getCommandLine().getOptionValue(OPT_f);
			if ("mycorps".equals(filterS)) {
				req.addProperty("filter", filterS);
			} else {
				float filter = filterS == null ? 1.0f : Float.parseFloat(filterS);
				req.addProperty("filter", filter);
			}

			return DupontReportJsonHandler.class.getName();

		} else if (cc.getCommandLine().hasOption(OPT_s)) {
			
			String code = cc.getCommandLine().getOptionValue(OPT_c);
			String yearS = cc.getCommandLine().getOptionValue(OPT_y);
			int year = yearS == null ? 2016 : Integer.parseInt(yearS);

			req.addProperty("corpId", code);
			req.addProperty("year", year);

			// filter
			String filterS = cc.getCommandLine().getOptionValue(OPT_f);
			if ("mycorps".equals(filterS)) {
				req.addProperty("filter", filterS);
			} else {
				float filter = filterS == null ? 1.0f : Float.parseFloat(filterS);
				req.addProperty("filter", filter);
			}

			return DupontSvgJsonHandler.class.getName();

		}
		return null;
	}


}
