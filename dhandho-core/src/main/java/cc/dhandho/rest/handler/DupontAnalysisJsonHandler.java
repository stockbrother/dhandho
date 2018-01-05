package cc.dhandho.rest.handler;

import com.google.gson.JsonObject;

import cc.dhandho.report.dupont.DupontAnalysis;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.util.JsonUtil;

public class DupontAnalysisJsonHandler extends AbstractRestRequestHandler {

	@Override
	public void handle(RestRequestContext arg0) {

		JsonObject req = (JsonObject) JsonUtil.parse(arg0.getReader());
		int year = req.get("year").getAsInt();
		DupontAnalysis dupontAnalysis = new DupontAnalysis(reportEngine);
		dupontAnalysis.analysisAndStore(year, dbProvider);

	}

}
