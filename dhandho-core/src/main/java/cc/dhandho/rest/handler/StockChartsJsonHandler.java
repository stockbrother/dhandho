package cc.dhandho.rest.handler;

import com.age5k.jcps.framework.container.Container;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.report.query.SvgChartMetricQueryBuilder;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.rest.server.DbProvider;

public class StockChartsJsonHandler extends AbstractRestRequestHandler {

	AllQuotesInfos aqis;
	DbReportMetaInfos aliasInfos;
	DbProvider dbProvider;

	@Override
	public void setContainer(Container app) {

		super.setContainer(app);
		this.dbProvider = app.findComponent(DbProvider.class, true);
		aqis = this.app.findComponent(AllQuotesInfos.class, true);
		aliasInfos = this.app.findComponent(DbReportMetaInfos.class, true);
	}

	@Override
	public void handleInternal(RestRequestContext rrc) throws Exception {

		SvgChartMetricQueryBuilder r = SvgChartMetricQueryBuilder.newInstance(rrc.getReader(), aliasInfos);

		StringBuffer sb = r.query(this.dbProvider);
		JsonWriter writer = rrc.getWriter();
		writer.beginObject();
		writer.name("type").value("svg");
		writer.name("svg").value(sb.toString());
		writer.endObject();

	}

}
