package cc.dhandho.graphdb.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.graphdb.OResultSetHandler;
import cc.dhandho.util.JsonUtil;

public class HeadLimitedResultSetHandler implements OResultSetHandler<JsonObject> {
	private static final Logger LOG = LoggerFactory.getLogger(HeadLimitedResultSetHandler.class);

	int limit;

	public HeadLimitedResultSetHandler() {
		this(100);
	}

	public HeadLimitedResultSetHandler(int limit) {
		this.limit = limit;
	}

	@Override
	public JsonObject handle(OResultSet arg0) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("begin handling result set,size if known:" + arg0.getExactSizeIfKnown());
		}
		JsonObject rt = new JsonObject();
		JsonArray arry = new JsonArray();
		rt.addProperty("sizeIfKnow", arg0.getExactSizeIfKnown());
		int i = 0;
		int limit = 0;
		while (arg0.hasNext()) {

			OResult r = arg0.next();
			String jsonI = r.toJSON();
			if (limit < this.limit) {
				arry.add(JsonUtil.parse(jsonI));//
				limit++;
			}

			i++;
		}
		rt.addProperty("sizeActual", i);
		rt.addProperty("limitToShow", this.limit);
		rt.add("rowArray", arry);
		if (LOG.isTraceEnabled()) {
			LOG.trace("end of handling of result set,size if known:" + arg0.getExactSizeIfKnown());
		}
		return rt;
	}

}
