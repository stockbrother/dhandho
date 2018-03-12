package cc.dhandho.rest.handler;

import java.util.Map;

import com.age5k.jcps.framework.container.Container;
import com.google.gson.JsonObject;

import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.dupont.DupontAnalysis;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.util.JsonUtil;
/**
 * 
 * @author Wu
 *
 */
public class DupontAnalysisJsonHandler extends AbstractRestRequestHandler {

	protected ReportEngine reportEngine;
	
	protected DbProvider dbProvider;
	
	@Override
	public void setContainer(Container app) {	
		super.setContainer(app);
		this.reportEngine = app.findComponent(ReportEngine.class, true);
		this.dbProvider = app.findComponent(DbProvider.class, true);
	}

	@Override
	public void handleInternal(RestRequestContext arg0) {

		JsonObject req = (JsonObject) JsonUtil.parse(arg0.getReader());
		int year = req.get("year").getAsInt();
		DupontAnalysis dupontAnalysis = new DupontAnalysis(reportEngine);
		Map<String,Object> result = dupontAnalysis.analysisAndStore(year, dbProvider);
		
		JsonObject res = JsonUtil.toJsonObject(result);
		arg0.write(res);
		
	}

}
