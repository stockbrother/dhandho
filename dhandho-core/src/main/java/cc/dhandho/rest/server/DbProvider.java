package cc.dhandho.rest.server;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.commons.handler.Handler2;
import cc.dhandho.commons.handler.Handler3;
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

}
