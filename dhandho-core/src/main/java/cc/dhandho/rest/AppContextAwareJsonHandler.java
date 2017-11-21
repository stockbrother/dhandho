package cc.dhandho.rest;

import cc.dhandho.AppContext;

public abstract class AppContextAwareJsonHandler implements JsonHandler, AppContext.Aware {

	protected AppContext app;

	@Override
	public void setAppContext(AppContext app) {
		this.app = app;
	}

}
