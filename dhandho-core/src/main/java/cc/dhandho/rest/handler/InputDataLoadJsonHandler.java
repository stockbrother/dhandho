package cc.dhandho.rest.handler;

import java.io.IOException;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.rest.server.InputDataMainLoader;

/**
 * 
 * 
 * @author Wu
 *
 */
public class InputDataLoadJsonHandler extends DbSessionJsonHandler {

	@Override
	public void execute(RestRequestContext rrc, ODatabaseSession db) throws IOException {
		InputDataMainLoader l = this.app.findComponent(InputDataMainLoader.class, true);
		l.execute();
	}

}
