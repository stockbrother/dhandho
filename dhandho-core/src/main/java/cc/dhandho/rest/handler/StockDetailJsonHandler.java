package cc.dhandho.rest.handler;

import com.age5k.jcps.framework.container.Container;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;

public class StockDetailJsonHandler extends AbstractRestRequestHandler {

	AllQuotesInfos aqis;
	ReportEngine reportEngine;

	@Override
	public void setContainer(Container app) {

		super.setContainer(app);
		aqis = this.app.findComponent(AllQuotesInfos.class, true);
		this.reportEngine = this.app.findComponent(ReportEngine.class, true);
	}

	@Override
	public void handleInternal(RestRequestContext rrc) throws Exception {
		JsonObject req = rrc.parseReader();
		String corpId = req.get("corpId").getAsString();
		JsonWriter writer = rrc.getWriter();

		writer.beginObject();
		writer.name("type").value("map");
		writer.name("mapData").beginObject();
		String corpName = this.reportEngine.getCorpName(corpId);

		writer.name("corpId").value(corpId);
		writer.name("corpName").value(corpName);

		Double price = this.aqis.getBuyPrice(corpId);
		if (price == null) {
			price = 0D;
		}

		writer.name("unitPrice").value(price);
		long priceDate = this.aqis.getLastLoaded();
		writer.name("priceDate").value(priceDate);
		String metric = "实收资本_或股本_";
		Double d = this.reportEngine.getMetricValue(corpId, 2016, metric);
		Double totalPrice = 0D;
		if (d != null && price != null) {
			totalPrice = d.doubleValue() * price.doubleValue();
		}
		writer.name("totalPrice").value(totalPrice);
		writer.endObject();
		writer.endObject();
	}

}
