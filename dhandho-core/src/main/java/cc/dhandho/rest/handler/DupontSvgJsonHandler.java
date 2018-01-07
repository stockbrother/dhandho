package cc.dhandho.rest.handler;

import java.io.IOException;

import com.age5k.jcps.JcpsException;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.report.chart.SvgChartWriter;
import cc.dhandho.report.dupont.PointsFilterDupontSvgBuilder;
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
		float filter = req.get("filter").getAsFloat();

		JsonWriter w = arg0.getWriter();
		try {
			w.beginObject();
			writeSvgField("svg1", corpId, year, AssetTurnover.class.getName(), ProfitMarginNode.class.getName(), filter,
					w);
			writeSvgField("svg2", corpId, year, AssetTurnover.class.getName(), EquityMultiplier.class.getName(), filter,
					w);
			writeSvgField("svg3", corpId, year, ProfitMarginNode.class.getName(), EquityMultiplier.class.getName(),
					filter, w);

			w.endObject();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	private void writeSvgField(String key, String corpId, int year, String xDefine, String yDefine, float filter,
			JsonWriter w) {

		SvgChartWriter writer = new SvgChartWriter();
		StringBuilder sb = new StringBuilder();
		writer.writeScatterSvg(new PointsFilterDupontSvgBuilder(year, new String[] { xDefine, yDefine }, dbProvider)
				.centerCorpId(corpId)//
				.filter(filter)//
				, xDefine, yDefine, new String[] { corpId }, sb);

		sb = SvgUtil.writeSvg2Html(600, 320, sb.toString(), new StringBuilder());

		try {

			w.name(key);
			w.value(sb.toString());

		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}

	}

}
