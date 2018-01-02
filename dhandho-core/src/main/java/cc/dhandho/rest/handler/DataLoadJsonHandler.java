package cc.dhandho.rest.handler;

import java.io.File;

import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;

/**
 * Load Washed Data to DB.
 * 
 * @author wu
 *
 */
public class DataLoadJsonHandler extends AbstractRestRequestHandler {

	@Override
	public void handle(RestRequestContext rrc) {

		File dir = new File("C:\\dhandho\\data\\xueqiu\\washed");

		// DbInitUtil.initDb(app);

		// new GDBWashedFileSchemaLoader(app, dir, Quarter.Q4)/* .limit(10) */.start();

		// new GDBWashedFileValueLoader(app, dir, Quarter.Q4)/* .limit(10) */.start();

	}

}
