package cc.dhandho.client.handler.rest;

import java.io.StringWriter;

import com.google.gson.JsonObject;

import cc.dhandho.client.CommandContext;
import cc.dhandho.client.RestRequestCommandHandler;
import cc.dhandho.client.handler.ShowDispatcherCommandHandler;
import cc.dhandho.client.rest.RestResponseContext;
import cc.dhandho.report.CorpDatedMetricReportData;
import cc.dhandho.rest.handler.ReportDataJsonHandler;
import cc.dhandho.util.JsonUtil;

public class ShowReportCommandHandler extends RestRequestCommandHandler {

	@Override
	protected String buildRequest(CommandContext cc, JsonObject req) {
		String code = cc.getCommandLine().getOptionValue(ShowDispatcherCommandHandler.OPT_c);
		String metricsS = cc.getCommandLine().getOptionValue(ShowDispatcherCommandHandler.OPT_m);
		String[] metrics = metricsS.split("/");
		
		req.addProperty("corpId", code);
		req.add("metrics", JsonUtil.toJsonArray(metrics));
		return ReportDataJsonHandler.class.getName();
	}

	@Override
	protected void onResponse(RestResponseContext rrc) {
		JsonObject res = (JsonObject)rrc.response;

		// Response
		CorpDatedMetricReportData r = CorpDatedMetricReportData.parseJson(res.get("report").getAsJsonObject());

		CorpDatedMetricReportData r2 = CorpDatedMetricReportData.parseJson(res.get("report2").getAsJsonObject());

		StringBuilder sb = new StringBuilder();
		sb.append("<table>");
		int colspan = 6;
		{
			toTr(r, sb, colspan);
		}
		{// middle tr

			sb.append("<tr>");
			toTdTd("Price", res.get("price"), sb);
			toTdTd("Capital", res.get("capital"), sb);
			toTdTd("TotoalPrice", res.get("totoalPrice"), sb);
			sb.append("</tr>");
		}
		{
			toTr(r2, sb, colspan);
		}

		CorpDatedMetricReportData r3 = r2.clone();
		r3.dividBy(1D);
		toTr(r3, sb, colspan);

		sb.append("</table>");
		rrc.getConsole().htmlRenderer.showHtml(sb);

		StringWriter writer = new StringWriter();
		JsonUtil.write(res, writer);
		rrc.commandContext.getWriter().writeLn(writer.getBuffer().toString());

	}

	private void toTdTd(String name, Object value, StringBuilder sb) {
		sb.append("<td>");
		sb.append(name + ":");
		sb.append("</td>");
		sb.append("<td>");
		sb.append(value);
		sb.append("</td>");
	}

	private void toTr(CorpDatedMetricReportData rd, StringBuilder sb, int colspan) {
		sb.append("<tr>");
		sb.append("<td colspan='" + colspan + "'>");
		rd.toSvgDivAndHtmlTable(sb);
		sb.append("</td>");
		sb.append("</tr>");
	}

}
