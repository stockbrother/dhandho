package cc.dhandho.client;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import com.google.gson.stream.JsonWriter;

import cc.dhandho.RtException;
import cc.dhandho.commons.commandline.CommandAndLine;
import cc.dhandho.rest.CorpChartJsonHandler;
import cc.dhandho.rest.JsonMetricSqlLinkQueryBuilder;
import cc.dhandho.util.JsonUtil;

/**
 * @see JsonMetricSqlLinkQueryBuilder
 * @author wu
 *
 */
public class CorpChartCommandHandler extends DhandhoCommandHandler {

	@Override
	public void execute(CommandAndLine line) {

		String[] args = line.getArgs();
		String corpId = args[0];
		String metricsS = args[1];
		String[] metrics = metricsS.split("/");

		int[] years = new int[] { 2016, 2015, 2014, 2013, 2012 };
		DhandhoConsole console = (DhandhoConsole) line.getConsole();

		StringWriter sWriter = new StringWriter();
		try {

			JsonWriter writer = JsonUtil.newJsonWriter(sWriter);
			console.getMetricsDefine().buildMetricRequestAsJson(corpId, years, metrics, writer);

			StringWriter sWriter2 = new StringWriter();
			console.getServer().handle(CorpChartJsonHandler.class.getName(), new StringReader(sWriter.toString()),
					sWriter2);
			System.out.println(sWriter2.toString());

		} catch (IOException e) {
			throw RtException.toRtException(e);
		}

	}

}
