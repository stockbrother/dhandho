package cc.dhandho.report.query;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.ReportMetaInfos;
import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.util.JsonUtil;

/**
 * <code>
 * String jsonS = (""//
				+ "  [" //
				+ "    { 'name':'净资产收益率'," //
				+ "      'offset':0," //
				+ "      'operator':'*'," //
				+ "      'children':[" //
				+ "        { 'name':'总资产收益率'," //
				+ "          'offset':0," //
				+ "          'operator':'/'," //
				+ "          'components':[" //
				+ "            '净利润'," //
				+ "            '资产总计'," //
				+ "          ]" //
				+ "        }," //
				+ "        { 'name':'权益乘数'," //
				+ "          'offset':0," //
				+ "          'operator':'/'," //
				+ "          'components':[" //
				+ "          '资产总计'," //
				+ "          '所有者权益_或股东权益_合计'," //
				+ "          ]" //
				+ "        }" //
				+ "      ]" //
				+ "    }" //
				+ "  ]" //				
		).replaceAll("'", "\"");
	</code>
 *
 * Join is not supported by orientdb. But link looks good.
 *
 * @author wu
 *
 */
public class JsonArrayMetricsQuery extends MetricsQuery<JsonArray> {

	private static Logger LOG = LoggerFactory.getLogger(JsonArrayMetricsQuery.class);

	public static JsonArrayMetricsQuery newInstance(JsonReader reader, ReportMetaInfos aliasInfos) {
		JsonArrayMetricsQuery rt = new JsonArrayMetricsQuery(reader, aliasInfos);
		return rt;
	}

	public JsonArrayMetricsQuery(JsonReader reader, ReportMetaInfos reportMetaInfos) {
		super(reader, reportMetaInfos);
	}

	@Override
	public JsonArray handle(OResultSet req) {
		return new JsonMetricQueryResultHandler().handle(req);
	}

}
