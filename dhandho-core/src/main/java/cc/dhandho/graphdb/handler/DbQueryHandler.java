package cc.dhandho.graphdb.handler;

import com.age5k.jcps.framework.handler.Handler3;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.OResultSetHandler;

public class DbQueryHandler<T> implements Handler3<ODatabaseSession, T> {

	protected String sql;
	protected OResultSetHandler<T> resultSetHandler;
	protected Object[] args = new Object[] {};

	public DbQueryHandler(String sql, OResultSetHandler<T> rsh) {
		this.sql = sql;
		this.resultSetHandler = rsh;
	}

	@Override
	public T handle(ODatabaseSession arg0) {

		return DbUtil.executeQuery(arg0, sql, args, resultSetHandler);
	}

}
