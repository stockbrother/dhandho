package cc.dhandho.rest.handler;

import java.io.IOException;
import java.io.StringWriter;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.RtException;
import cc.dhandho.commons.handler.Handler2;
import cc.dhandho.report.query.JsonArrayMetricsQuery;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.util.JsonUtil;

/**
 * 
 * @author Wu
 *
 */
public class GetPriceRatioJsonHandler extends AbstractRestRequestHandler {

	@Override
	public void handle(RestRequestContext rrc) {

		JsonObject json = (JsonObject) rrc.parseReader();
		String corpId = json.get("corpId").getAsString();
		AllQuotesInfos aqis = this.app.findComponent(AllQuotesInfos.class, true);
		Double value = aqis.get(corpId);

		JsonObject res = new JsonObject();
		res.addProperty("corpId", corpId);
		res.addProperty("price", value);

		StringWriter sWriter = new StringWriter();
		JsonWriter writer2 = new JsonWriter(sWriter);

		this.dbProvider.executeWithDbSession(new Handler2<ODatabaseSession>() {

			@Override
			public void handle(ODatabaseSession t) {
				try {
					queryMetrics(corpId, t, writer2);
				} catch (IOException e) {
					throw RtException.toRtException(e);
				}
			}
		});
		// TODO rewrite json by price.
		JsonWriter writer = rrc.getWriter();
		try {

			writer.beginObject();
			writer.name("corpId").value(corpId);
			writer.name("price").value(value);
			writer.name("metrics:");
			writer.jsonValue(sWriter.getBuffer().toString());
			writer.endObject();
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}

	}

	private void queryMetrics(String corpId, ODatabaseSession db, JsonWriter writer) throws IOException {

		DbReportMetaInfos aliasInfos = new DbReportMetaInfos();
		aliasInfos.initialize(db);

		String jsonS = ("{" //
				+ "'corpId':'" + corpId + "'"// end of corpId
				+ ",'dates':[2016,2015,2014,2013,2012]"// end of years
				+ ",'metrics':" //
				+ " ["//
				+ "  '净利润'," //
				+ "  '资产总计'," //
				+ "  '所有者权益_或股东权益_合计'" //
				+ " ]"//
				+ "}" // end of message
		).replaceAll("'", "\"");

		JsonReader reader = JsonUtil.toJsonReader(jsonS);

		JsonArrayMetricsQuery r = JsonArrayMetricsQuery.newInstance(reader, aliasInfos);
		r.query(db);

	}

}
