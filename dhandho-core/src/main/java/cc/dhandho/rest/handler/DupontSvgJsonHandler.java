package cc.dhandho.rest.handler;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

import com.age5k.jcps.JcpsException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.report.chart.SvgChartWriter;
import cc.dhandho.report.dupont.CenterPointDistanceBasedFilterDupontPointFinder;
import cc.dhandho.report.dupont.CorpFilter;
import cc.dhandho.report.dupont.CorpPoint;
import cc.dhandho.report.dupont.DupontPointFinder;
import cc.dhandho.report.dupont.node.AssetTurnover;
import cc.dhandho.report.dupont.node.EquityMultiplier;
import cc.dhandho.report.dupont.node.ProfitMarginNode;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.util.JsonUtil;
import cc.dhandho.util.SvgUtil;

public class DupontSvgJsonHandler extends AbstractRestRequestHandler {

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
		try {
			w.beginObject();
			w.name("corpPointsHeader");
			JsonUtil.array(types, w);
			writeCorpPoint("corpPointsBody", map, corpId, w);
			writeSvgField("svg1", types[0], 0, types[1], 1, map, corpId, w);
			writeSvgField("svg2", types[2], 2, types[1], 1, map, corpId, w);
			writeSvgField("svg3", types[0], 0, types[2], 2, map, corpId, w);

			w.endObject();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	private String getCorpName(String corpId) {
		return this.reportEngine.getCorpName(corpId);
	}

	private void writeCorpPoint(String key, Map<String, CorpPoint> map, String corpId, JsonWriter w) {
		try {
			w.name(key);
			w.beginArray();
			map.values().stream().forEach(new Consumer<CorpPoint>() {

				@Override
				public void accept(CorpPoint t) {
					try {

						w.beginObject();
						w.name("corpId").value(t.corpId);
						String corpName = getCorpName(t.corpId);
						w.name("corpName").value(corpName);
						boolean highLight = corpId.equals(t.corpId);
						w.name("highLight").value(highLight);
						w.name("vector");
						w.beginArray();
						for (Double d : t.point) {
							w.value(d);
						}
						w.endArray();
						w.endObject();

					} catch (IOException e) {
						throw JcpsException.toRtException(e);
					}
				}
			});

			w.endArray();

		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	private void writeSvgField(String key, String yType, int yIdx, String xType, int xIdx, Map<String, CorpPoint> map,
			String corpId, JsonWriter w) {

		String xLabel = xType.substring(xType.lastIndexOf(".") + 1);
		String yLabel = yType.substring(xType.lastIndexOf(".") + 1);

		SvgChartWriter writer = new SvgChartWriter();
		StringBuilder sb = new StringBuilder();
		writer.writeScatterSvg(xLabel, xIdx, yLabel, yIdx, map, new String[] { corpId }, sb);

		sb = SvgUtil.writeSvg2Html(600, 320, sb.toString(), new StringBuilder());

		try {

			w.name(key);
			w.value(sb.toString());

		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}

	}

}
