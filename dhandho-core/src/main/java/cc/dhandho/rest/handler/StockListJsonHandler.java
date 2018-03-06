package cc.dhandho.rest.handler;

import java.io.IOException;

import com.age5k.jcps.JcpsException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.OResultSetHandler;
import cc.dhandho.graphdb.dataver.DbUpgrader0_0_1;
import cc.dhandho.rest.RestRequestContext;

public class StockListJsonHandler extends DbSessionJsonHandler {

	@Override
	protected void execute(RestRequestContext rrc, ODatabaseSession dbs) throws IOException {

		String sql = "select from " + DbUpgrader0_0_1.V_CORP_INFO + " where 1=1 ";

		DbUtil.executeQuery(dbs, sql, new Object[] {}, new OResultSetHandler<Object>() {

			@Override
			public Object handle(OResultSet rst) {

				try {

					return doProcess(rst, rrc.getReader(), rrc.getWriter());

				} catch (IOException e) {
					throw JcpsException.toRtException(e);
				}
			}
		});
	}

	private Object doProcess(OResultSet rst, JsonReader reader, JsonWriter writer) throws IOException {
		while (rst.hasNext()) {
			OVertex v = rst.next().getVertex().get();
			
		}
		return null;
	}

}
