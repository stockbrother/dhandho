package cc.dhandho.rest.handler;

import java.io.IOException;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.RtException;
import cc.dhandho.commons.handler.Handler2;
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
					throw RtException.toRtException(e);
				}
			}
		});

	}

	protected abstract void execute(RestRequestContext rrc, ODatabaseSession db)
			throws IOException;

}
