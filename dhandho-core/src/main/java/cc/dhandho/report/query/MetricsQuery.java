package cc.dhandho.report.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.framework.handler.Handler3;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.ReportMetaInfos;
import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.OResultSetHandler;
import cc.dhandho.rest.server.DbProvider;

public abstract class MetricsQuery<T> implements OResultSetHandler<T> {
	private static Logger LOG = LoggerFactory.getLogger(MetricsQuery.class);

	protected ReportMetaInfos aliasInfos;

	protected QueryJsonWrapper query;
	
	protected JsonObject json;
	
	public MetricsQuery(JsonObject json, ReportMetaInfos reportMetaInfos) {
		this.json = json;
		this.aliasInfos = reportMetaInfos;		
	}
	public MetricsQuery(JsonReader reader, ReportMetaInfos reportMetaInfos) {
		JsonElement json = Streams.parse(reader);
		this.json = json.getAsJsonObject();
		this.aliasInfos = reportMetaInfos;
	}

	public T query(DbProvider dbProvider) {
		return dbProvider.executeWithDbSession(new Handler3<ODatabaseSession, T>() {

			@Override
			public T handle(ODatabaseSession req) {
				return query(req);
			}
		});
	}

	public T query(ODatabaseSession db) {

		StringBuffer sql = new StringBuffer();

		
		this.query = new QueryJsonWrapper(json);
		
		this.query.doBuildSql(sql, this.aliasInfos);

		String corpId = this.query.getCorpId();

		LOG.info("sql:" + sql + ",corpId:" + corpId);

		return DbUtil.executeQuery(db, sql.toString(), this.query.getParameterArray(), this);

	}

}
