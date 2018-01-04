package cc.dhandho.graphdb;

import com.age5k.jcps.framework.handler.Handler2;
import com.age5k.jcps.framework.handler.Handler3;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;

import cc.dhandho.rest.server.DbProvider;

public abstract class AbstractDbProvider implements DbProvider {

	protected abstract OrientDB getOrient();

	protected abstract ODatabaseSession openDB() ;
		
	@Override
	public void executeWithDbSession(Handler2<ODatabaseSession> processor) {

		executeWithDbSession(new Handler3<ODatabaseSession, Void>() {

			@Override
			public Void handle(ODatabaseSession req) {
				processor.handle(req);
				return null;
			}
		});

	}

	@Override
	public <R> R executeWithDbSession(Handler3<ODatabaseSession, R> processor) {
		ODatabaseSession dbs = DbSessionTL.get();
		boolean isNew = (dbs == null);
		if (isNew) {
			dbs = this.openDB();
			DbSessionTL.set(dbs);
		}

		try {
			return processor.handle(dbs);
		} finally {
			if (isNew) {
				DbSessionTL.set(null);//
				dbs.close();
			}
		}
	}

}
