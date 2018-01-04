package cc.dhandho.graphdb;

import com.age5k.jcps.framework.handler.Handler2;
import com.age5k.jcps.framework.handler.Handler3;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;

import cc.dhandho.rest.server.DbProvider;

public class DefaultDbProvider extends AbstractDbProvider {
	protected DbConfig dbConfig;

	protected OrientDB orient;
	
	@Override
	protected OrientDB getOrient() {
		if (orient == null) {
			orient = new OrientDB(this.dbConfig.getDbUrl(), OrientDBConfig.defaultConfig());
		}
		return orient;
	}

	@Override
	public DbProvider dbConfig(DbConfig dbConfig) {
		this.dbConfig = dbConfig;
		return this;
	}

	@Override
	public boolean createDbIfNotExist() {
		return this.getOrient().createIfNotExists(this.dbConfig.getDbName(), this.dbConfig.getDbType());
	}

	@Override
	protected ODatabaseSession openDB() {
		// TODO avoid openDB twice.
		// TODO if dbName = null, it will block here. why?
		if (this.dbConfig.getDbName() == null) {
			throw new IllegalArgumentException("no db name set.");
		}

		ODatabaseSession databaseSession = this.getOrient().open(this.dbConfig.getDbName(), this.dbConfig.getUser(),
				this.dbConfig.getPassword());

		return databaseSession;
	}

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
