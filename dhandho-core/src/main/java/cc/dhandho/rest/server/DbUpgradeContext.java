package cc.dhandho.rest.server;

import com.orientechnologies.orient.core.db.ODatabaseSession;

public class DbUpgradeContext {

	private ODatabaseSession dbs;

	public DbUpgradeContext(ODatabaseSession dbs) {
		this.dbs = dbs;
	}

	public ODatabaseSession getDbs() {
		return dbs;
	}

}
