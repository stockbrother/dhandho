package cc.dhandho.rest;

import cc.dhandho.DhandhoHome;
import cc.dhandho.commons.container.Container;
import cc.dhandho.server.DbProvider;

/**
 * AppContext.newInstance();
 * @author Wu
 *
 */
public abstract class AbstractRestRequestHandler implements RestRequestHandler, Container.Aware {

	protected Container app;

	protected DbProvider dbProvider;
	
	protected DhandhoHome home;

	@Override
	public void setContainer(Container app) {
		this.app = app;
		this.dbProvider = app.findComponent(DbProvider.class, true);
		this.home = app.findComponent(DhandhoHome.class, true);
	}

}
