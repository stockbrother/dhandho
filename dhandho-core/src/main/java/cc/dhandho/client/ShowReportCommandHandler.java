package cc.dhandho.client;

import java.io.IOException;
import java.io.StringWriter;

import com.google.gson.JsonObject;

import cc.dhandho.RtException;
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
			StringWriter writer = new StringWriter();
			JsonUtil.write(res, writer);
			cc.getWriter().write(writer.getBuffer().toString());
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}
	}

}
