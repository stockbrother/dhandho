package cc.dhandho.server;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.Processor;
import cc.dhandho.graphdb.DbConfig;

public interface DbProvider {
	public DhandhoServer dbConfig(DbConfig dbConfig);

	public boolean createDbIfNotExist();
	
	public ODatabaseSession openDB() ;
	
	public void executeWithDbSession(Processor<ODatabaseSession> processor) ;
	
	public ODatabaseSession getDB();
}
