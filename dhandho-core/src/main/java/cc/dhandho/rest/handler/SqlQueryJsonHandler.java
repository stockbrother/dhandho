package cc.dhandho.rest.handler;

import com.google.gson.JsonObject;

import cc.dhandho.graphdb.handler.DbQueryHandler;
import cc.dhandho.graphdb.handler.HeadLimitedResultSetHandler;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;

/**
 * Load Washed Data to DB.
 * 
 * @author wu
 *
 */
public class SqlQueryJsonHandler extends AbstractRestRequestHandler {

	@Override
	public void handle(RestRequestContext rrc) {
		JsonObject req = (JsonObject) rrc.parseReader();
		String sql = req.get("sql").getAsString();

		JsonObject json = this.dbProvider
				.executeWithDbSession(new DbQueryHandler<JsonObject>(sql, new HeadLimitedResultSetHandler()));
		rrc.write(json);
	}

}
