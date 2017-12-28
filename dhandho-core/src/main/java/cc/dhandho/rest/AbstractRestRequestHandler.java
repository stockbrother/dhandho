package cc.dhandho.rest;

import cc.dhandho.AppContext;
import cc.dhandho.DhandhoHome;
import cc.dhandho.server.DbProvider;

/**
 * AppContext.newInstance();
 * @author Wu
 *
 */
public abstract class AbstractRestRequestHandler implements RestRequestHandler, AppContext.Aware {

	protected AppContext app;

	protected DbProvider dbProvider;
	
	protected DhandhoHome home;

	@Override
	public void setAppContext(AppContext app) {
		this.app = app;
		this.dbProvider = app.findComponent(DbProvider.class, true);
		this.home = app.findComponent(DhandhoHome.class, true);
	}

}
