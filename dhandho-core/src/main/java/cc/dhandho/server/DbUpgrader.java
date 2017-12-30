package cc.dhandho.server;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.Processor;
import cc.dhandho.commons.container.Container;

public abstract class DbUpgrader implements Container.Aware, Processor<ODatabaseSession> {
	protected Container app;

	@Override
	public void setContainer(Container app) {
		this.app = app;
	}

}
