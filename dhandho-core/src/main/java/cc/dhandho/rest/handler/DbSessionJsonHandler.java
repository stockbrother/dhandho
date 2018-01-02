package cc.dhandho.rest.handler;

import java.io.IOException;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.handler.Handler2;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;

/**
 * 
 * @author Wu
 *
 */
public abstract class DbSessionJsonHandler extends AbstractRestRequestHandler {

	@Override
	public void handle(RestRequestContext rrc) {

		this.dbProvider.executeWithDbSession(new Handler2<ODatabaseSession>() {

			@Override
			public void handle(ODatabaseSession db) {
				try {
					execute(rrc, db);
				} catch (IOException e) {
					throw JcpsException.toRtException(e);
				}
			}
		});

	}

	protected abstract void execute(RestRequestContext rrc, ODatabaseSession db)
			throws IOException;

}
