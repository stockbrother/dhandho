package cc.dhandho.graphdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;

import cc.dhandho.Processor;
import cc.dhandho.rest.DbSessionTL;
import cc.dhandho.server.DbProvider;

public class DefaultDbProvider implements DbProvider {
	DbConfig dbConfig;

	OrientDB orient;

	private OrientDB getOrient() {
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

	private ODatabaseSession openDB() {
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
	public void executeWithDbSession(Processor<ODatabaseSession> processor) {

		ODatabaseSession dbs = DbSessionTL.get();
		boolean isNew = (dbs == null);
		if (isNew) {
			dbs = this.openDB();
			DbSessionTL.set(dbs);
		}

		try {
			processor.process(dbs);
		} finally {
			if (isNew) {
				DbSessionTL.set(null);//
				dbs.close();
			}
		}

	}

}
