package cc.dhandho.rest;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.AppContext;
import cc.dhandho.server.DbProvider;

/**
 * 
 * @author wuzhen
 *
 */
public abstract class AppContextAwareJsonHandler implements JsonHandler, AppContext.Aware {

	protected AppContext app;

	protected DbProvider dbProvider;

	@Override
	public void setAppContext(AppContext app) {
		this.app = app;
		this.dbProvider = app.findComponent(DbProvider.class, true);
	}

}
