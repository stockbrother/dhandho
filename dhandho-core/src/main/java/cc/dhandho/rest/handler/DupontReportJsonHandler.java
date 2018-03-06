package cc.dhandho.rest.handler;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.provider.Provider;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.mycorp.MyCorps;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.dupont.CenterPointDistanceBasedFilterDupontPointFinder;
import cc.dhandho.report.dupont.CorpFilter;
import cc.dhandho.report.dupont.CorpPoint;
import cc.dhandho.report.dupont.DupontPointFinder;
import cc.dhandho.report.dupont.node.AssetTurnover;
import cc.dhandho.report.dupont.node.EquityMultiplier;
import cc.dhandho.report.dupont.node.ProfitMarginNode;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.rest.TableJsonWriter;
import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.util.JsonUtil;

public class DupontReportJsonHandler extends AbstractRestRequestHandler {
	
	protected ReportEngine reportEngine;
	
	protected DbProvider dbProvider;
	
	protected Provider<MyCorps> myCorpsProvider;
	
	@Override
	public void setContainer(Container app) {	
		super.setContainer(app);
		this.reportEngine = app.findComponent(ReportEngine.class, true);
		this.dbProvider = app.findComponent(DbProvider.class, true);
		this.myCorpsProvider = app.findComponentLater(MyCorps.class, true);
	}
	
	@Override
	public void handle(RestRequestContext arg0) {
		JsonObject req = (JsonObject) JsonUtil.parse(arg0.getReader());
		String corpId = req.get("corpId").getAsString();
		int year = req.get("year").getAsInt();
		JsonElement filterJ = req.get("filter");
		CorpFilter filter = CorpFilter.valueOf(filterJ);

		String[] types = new String[] { ProfitMarginNode.class.getName(), AssetTurnover.class.getName(),
				EquityMultiplier.class.getName() };

		DupontPointFinder dpf = new CenterPointDistanceBasedFilterDupontPointFinder(year, types, dbProvider)//
				.myCorpsProvider(this.myCorpsProvider)//
				.centerCorpId(corpId)//
				.filter(filter)//
		;

		Map<String, CorpPoint> map = dpf.find();

		JsonWriter w = arg0.getWriter();
		// TODO use new JsonObject,not use writer?
		try {
			TableJsonWriter tw = new TableJsonWriter();
			tw.columns(new String[] { "corpId", "corpName", "highLight", "ProfitMargin", "AssetTurnover",
					"EquityMultiplier" });
			map.values().stream().forEach(new Consumer<CorpPoint>() {

				@Override
				public void accept(CorpPoint t) {
					JsonPrimitive corpIdI = new JsonPrimitive(t.corpId);
					JsonPrimitive corpName = new JsonPrimitive(getCorpName(t.corpId));
					JsonPrimitive highLight = new JsonPrimitive(corpId.equals(t.corpId));
					JsonPrimitive p0 = new JsonPrimitive(t.point[0]);
					JsonPrimitive p1 = new JsonPrimitive(t.point[1]);
					JsonPrimitive p2 = new JsonPrimitive(t.point[2]);

					JsonElement[] rowA = new JsonElement[] { corpIdI, corpName, highLight, p0, p1, p2 };
					tw.addRow(rowA);
				}
			});
			w.jsonValue(tw.getJson().toString());

		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	private String getCorpName(String corpId) {
		return this.reportEngine.getCorpName(corpId);
	}

}
