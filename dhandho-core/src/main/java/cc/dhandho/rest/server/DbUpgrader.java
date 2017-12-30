package cc.dhandho.rest.server;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.commons.container.Container;
import cc.dhandho.commons.handler.Handler2;

public abstract class DbUpgrader implements Container.Aware, Handler2<ODatabaseSession> {
	protected Container app;

	@Override
	public void setContainer(Container app) {
		this.app = app;
	}

}
