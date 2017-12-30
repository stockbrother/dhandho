package cc.dhandho.client;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.RtException;
import cc.dhandho.commons.commandline.CommandAndLine;
import cc.dhandho.commons.commandline.CommandLineWriter;
import cc.dhandho.report.query.JsonArrayMetricsQuery;
import cc.dhandho.rest.handler.CorpChartJsonHandler;
import cc.dhandho.util.JsonUtil;

/**
 * @see JsonArrayMetricsQuery
 * @author wu
 *
 */
public class CorpChartCommandHandler extends DhandhoCommandHandler {

	@Override
	public void execute(CommandContext cc) {

		String[] args = cc.getArgs();
		String corpId = args[0];
		String metricsS = args[1];
		String[] metrics = metricsS.split("/");

		int[] years = new int[] { 2016, 2015, 2014, 2013, 2012 };

		StringWriter sWriter = new StringWriter();
		try {

			JsonWriter writer = JsonUtil.newJsonWriter(sWriter);
			cc.getConsole().getMetricsDefine().buildMetricRequestAsJson(corpId, years, metrics, writer);

			StringWriter sWriter2 = new StringWriter();
			cc.getConsole().getServer().handle(CorpChartJsonHandler.class.getName(), new StringReader(sWriter.toString()),
					sWriter2);

			JsonObject jsonO = (JsonObject) JsonUtil.parse(sWriter2.getBuffer().toString());
			int height = jsonO.get("height").getAsInt();
			int width = jsonO.get("width").getAsInt();
			String svg = jsonO.get("svg").getAsString();

			StringWriter sWriter3 = new StringWriter();
			writeSvg2Html(width, height, svg, sWriter3);
			cc.getConsole().htmlRenderer.showHtml(sWriter3.getBuffer().toString());
			
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}

	}

	private static void writeSvg2Html(int width, int height, String svg, Writer out) throws IOException {
		out.write("<html>\n");
		out.write("  <head>\n");
		out.write("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n");
		out.write("  </head>\n");
		out.write("  <body>\n");
		out.write("    <div style=\"width:" + width + "; height:" + height + ";\">\n");
		out.write("    " + svg + "\n");
		out.write("    </div>\n");
		out.write("  </body>\n");
		out.write("</html>\n");
		out.flush();
	}
}
