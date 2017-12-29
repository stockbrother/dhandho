package cc.dhandho.report;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.AliasInfos;
import cc.dhandho.RtException;
import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.GDBResultSetProcessor;

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
public class JsonMetricSqlLinkQueryBuilder {

	private static Logger LOG = LoggerFactory.getLogger(JsonMetricSqlLinkQueryBuilder.class);

	public static JsonMetricSqlLinkQueryBuilder newInstance(JsonReader reader, AliasInfos aliasInfos) {
		JsonMetricSqlLinkQueryBuilder rt = new JsonMetricSqlLinkQueryBuilder(reader, aliasInfos);
		return rt;
	}

	private JsonReader reader;

	AliasInfos aliasInfos;

	StringBuffer sql;

	public QueryJsonWrapper query;

	protected JsonMetricSqlLinkQueryBuilder(JsonReader reader, AliasInfos aliasInfos) {
		this.reader = reader;
		this.aliasInfos = aliasInfos;
	}

	public JsonMetricSqlLinkQueryBuilder query(ODatabaseSession db, JsonWriter writer) throws IOException {

		String sql = this.getSql();
		String corpId = this.query.getCorpId();

		LOG.info("sql:" + sql + ",corpId:" + corpId);

		DbUtil.executeQuery(db, sql, this.query.getParameterArray(), new GDBResultSetProcessor<Void>() {

			@Override
			public Void process(OResultSet rst) {
				try {

					writer.beginArray();
					while (rst.hasNext()) {
						writer.beginObject();
						OResult row = rst.next();

						for (String key : row.getPropertyNames()) {
							writer.name(key);
							Object value = row.getProperty(key);
							String jsonV = row.toJson(value);
							writer.jsonValue(jsonV);
						}

						writer.endObject();
					}
					writer.endArray();
				} catch (IOException e) {
					throw RtException.toRtException(e);
				}
				return null;
			}
		});

		return this;
	}

	public JsonMetricSqlLinkQueryBuilder build() throws IOException {

		this.sql = new StringBuffer();

		JsonElement json = Streams.parse(reader);
		JsonObject query = json.getAsJsonObject();
		this.query = new QueryJsonWrapper(query);
		this.query.doBuildSql(this);

		return this;
	}

	public String getSql() {
		return sql.toString();
	}

}
