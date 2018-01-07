package cc.dhandho.client.handler;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.age5k.jcps.JcpsException;
import com.google.gson.JsonObject;

import cc.dhandho.client.CommandContext;
import cc.dhandho.rest.handler.DupontAnalysisJsonHandler;
import cc.dhandho.rest.handler.DupontSvgJsonHandler;

public class DupontAnalysisCommandHandler extends DhandhoCommandHandler {

	public static final String OPT_a = "a";
	public static final String OPT_s = "s";
	public static final String OPT_c = "c";
	public static final String OPT_y = "y";
	public static final String OPT_f = "f";

	@Override
	public void execute(CommandContext cc) {

		if (cc.getCommandLine().hasOption(OPT_a)) {
			String yearS = cc.getCommandLine().getOptionValue(OPT_y);
			int year = yearS == null ? 2016 : Integer.parseInt(yearS);
			JsonObject req = new JsonObject();
			req.addProperty("year", year);
			cc.getServer().handle(DupontAnalysisJsonHandler.class.getName(), req);
			cc.consume();
		} else if (cc.getCommandLine().hasOption(OPT_s)) {

			String code = cc.getCommandLine().getOptionValue(OPT_c);
			String yearS = cc.getCommandLine().getOptionValue(OPT_y);
			String filterS = cc.getCommandLine().getOptionValue(OPT_f);
			float filter = filterS == null ? 1.0f : Float.parseFloat(filterS);
			int year = yearS == null ? 2016 : Integer.parseInt(yearS);
			JsonObject req = new JsonObject();
			req.addProperty("corpId", code);
			req.addProperty("year", year);
			req.addProperty("filter", filter);

			JsonObject res = (JsonObject) cc.getServer().handle(DupontSvgJsonHandler.class.getName(), req);

			String svg1 = res.get("svg1").getAsString();
			String svg2 = res.get("svg2").getAsString();
			String svg3 = res.get("svg3").getAsString();
			StringWriter sW = new StringWriter();
			writeSvg2Html(600, 320, new String[] { svg1, svg2, svg3 }, sW);
			cc.getConsole().htmlRenderer.showHtml(sW.getBuffer().toString());
			cc.consume();
		}

	}

	private static void writeSvg2Html(int width, int height, String[] svgA, Writer out) {
		try {

			out.write("<html>\n");
			out.write("  <head>\n");
			out.write("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n");
			out.write("  </head>\n");
			out.write("  <body>\n");
			for (String svg : svgA) {
				out.write("    <div style=\"width:" + width + "; height:" + height + ";\">\n");
				out.write("    " + svg + "\n");
				out.write("    </div>\n");
			}
			out.write("  </body>\n");
			out.write("</html>\n");
			out.flush();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}
}
