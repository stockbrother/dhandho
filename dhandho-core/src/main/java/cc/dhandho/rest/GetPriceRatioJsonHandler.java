package cc.dhandho.rest;

import java.io.IOException;
import java.io.StringWriter;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.Processor;
import cc.dhandho.RtException;
import cc.dhandho.report.JsonMetricSqlLinkQueryBuilder;
import cc.dhandho.util.JsonUtil;

/**
 * 
 * @author Wu
 *
 */
public class GetPriceRatioJsonHandler extends AbstractRestRequestHandler {

	@Override
	public void execute(RestRequestContext rrc) throws IOException {

		JsonObject json = (JsonObject) rrc.parseReader();
		String corpId = json.get("corpId").getAsString();
		AllQuotesInfos aqis = this.app.findComponent(AllQuotesInfos.class, true);
		Double value = aqis.get(corpId);

		JsonObject res = new JsonObject();
		res.addProperty("corpId", corpId);
		res.addProperty("price", value);

		StringWriter sWriter = new StringWriter();
		JsonWriter writer2 = new JsonWriter(sWriter);

		this.dbProvider.executeWithDbSession(new Processor<ODatabaseSession>() {

			@Override
			public void process(ODatabaseSession t) {
				try {
					queryMetrics(corpId, t, writer2);
				} catch (IOException e) {
					throw RtException.toRtException(e);
				}
			}
		});
		// TODO rewrite json by price.
		JsonWriter writer = rrc.getWriter();
		writer.beginObject();
		writer.name("corpId").value(corpId);
		writer.name("price").value(value);
		writer.name("metrics:");
		writer.jsonValue(sWriter.getBuffer().toString());
		writer.endObject();

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

		JsonMetricSqlLinkQueryBuilder r = JsonMetricSqlLinkQueryBuilder.newInstance(reader, aliasInfos);
		r.build().query(db, writer);

	}

}
