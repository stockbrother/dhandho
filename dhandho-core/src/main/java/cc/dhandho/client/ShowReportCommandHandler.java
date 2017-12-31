package cc.dhandho.client;

import java.io.IOException;
import java.io.StringWriter;

import com.google.gson.JsonObject;

import cc.dhandho.RtException;
import cc.dhandho.report.ReportData;
import cc.dhandho.rest.handler.ReportDataJsonHandler;
import cc.dhandho.util.JsonUtil;

public class ShowReportCommandHandler extends DhandhoCommandHandler {
	
	@Override
	public void execute(CommandContext cc) {
		String code = cc.getCommandLine().getOptionValue(ShowCommandHandler.OPT_c);
		String metricsS = cc.getCommandLine().getOptionValue(ShowCommandHandler.OPT_m);
		String[] metrics = metricsS.split("/");
		JsonObject req = new JsonObject();
		req.addProperty("corpId", code);
		req.add("metrics", JsonUtil.toJsonArray(metrics));

		try {
			JsonObject res = (JsonObject) cc.getServer().handle(ReportDataJsonHandler.class.getName(), req);
			
			ReportData r = ReportData.parseJson(res.get("report").getAsJsonObject());
			
			ReportData r2 = ReportData.parseJson(res.get("report2").getAsJsonObject());
			
			StringBuilder sb = new StringBuilder();
			sb.append("<table>");
			sb.append("<tr>");
			sb.append("<td colspan='6'>");
			r.toHtml(sb);
			sb.append("</td>");
			sb.append("</tr>");
			
			sb.append("<tr>");
			sb.append("<td>");
			sb.append("price:");
			sb.append("</td>");			
			sb.append("<td>");
			sb.append(res.get("price"));
			sb.append("</td>");
			
			sb.append("<td>");
			sb.append("totalPrice:");
			sb.append("</td>");
			
			sb.append("<td>");
			sb.append("capital:");
			sb.append("</td>");			
			sb.append("<td>");			
			sb.append(res.get("capital"));
			sb.append("</td>");
			
			sb.append("<td>");
			sb.append(res.get("totoalPrice"));
			sb.append("</td>");
			
			sb.append("</tr>");
			
			sb.append("<tr>");
			sb.append("<td colspan='6'>");			
			r2.toHtml(sb);			
			sb.append("</td>");
			sb.append("</tr>");
			
			sb.append("</table>");
			cc.getConsole().htmlRenderer.showHtml(sb);
			
			StringWriter writer = new StringWriter();
			JsonUtil.write(res, writer);
			cc.getWriter().write(writer.getBuffer().toString());
			
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}
	}

}
