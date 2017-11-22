package cc.dhandho.server;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.AppContext;

public abstract class DbUpgrader implements AppContext.Aware {
	protected AppContext app;

	@Override
	public void setAppContext(AppContext app) {
		this.app = app;
	}

	public abstract void upgrade(ODatabaseSession db);
}
