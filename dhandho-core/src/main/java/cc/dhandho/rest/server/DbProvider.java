package cc.dhandho.rest.server;

import com.age5k.jcps.framework.handler.Handler2;
import com.age5k.jcps.framework.handler.Handler3;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.graphdb.DbConfig;

/**
 * Data source.
 * 
 * @author Wu
 *
 */
public interface DbProvider {
	public DbProvider dbConfig(DbConfig dbConfig);

	public boolean createDbIfNotExist();

	public void executeWithDbSession(Handler2<ODatabaseSession> processor);

	public <R> R executeWithDbSession(Handler3<ODatabaseSession, R> processor);

	public void close();
	
}
