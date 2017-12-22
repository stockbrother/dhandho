package cc.dhandho.server;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.AppContext;
import cc.dhandho.Processor;

public abstract class DbUpgrader implements AppContext.Aware, Processor<ODatabaseSession> {
	protected AppContext app;

	@Override
	public void setAppContext(AppContext app) {
		this.app = app;
	}

}
