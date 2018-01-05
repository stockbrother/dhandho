package cc.dhandho.rest.handler;

import java.io.IOException;

import com.age5k.jcps.JcpsException;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.report.dupont.DupontAnalysis;
import cc.dhandho.report.dupont.node.AssetTurnover;
import cc.dhandho.report.dupont.node.DefineNode;
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

		DupontAnalysis dupontAnalysis = new DupontAnalysis(reportEngine);

		JsonWriter w = arg0.getWriter();
		try {
			w.beginObject();
			writeSvgField("svg1", corpId, year, AssetTurnover.class, ProfitMarginNode.class, w, dupontAnalysis);
			writeSvgField("svg2", corpId, year, AssetTurnover.class, EquityMultiplier.class, w, dupontAnalysis);
			w.endObject();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	private <X extends DefineNode, Y extends DefineNode> void writeSvgField(String key, String corpId, int year,
			Class<X> xDefine, Class<Y> yDefine, JsonWriter w, DupontAnalysis dupontAnalysis) {

		String[] heighLightCorpId = new String[] { corpId };
		StringBuilder sb = dupontAnalysis.buildScatterSvg(xDefine, yDefine, year, heighLightCorpId, dbProvider,
				new StringBuilder());
		sb = SvgUtil.writeSvg2Html(600, 320, sb.toString(), new StringBuilder());

		try {

			w.name(key);
			w.value(sb.toString());

		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}

	}

}
