package cc.dhandho.graphdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

public class DbUtil {

	@Deprecated // use DbQueryHandler instead.
	public static <T> T executeQuery(ODatabaseSession session, String sql, OResultSetHandler<T> resultProcessor) {
		return executeQuery(session, sql, new Object[] {}, resultProcessor);
	}

	public static <T> T executeQuery(ODatabaseSession session, String sql, Object[] args,
			OResultSetHandler<T> resultProcessor) {
		OResultSet rst = session.query(sql, args);
		try {
			T rt = resultProcessor.handle(rst);
			return rt;
		} finally {
			rst.close();
		}

	}

	public static void executeUpdate(ODatabaseSession session, String sql, Object[] args) {
		OResultSet rst = session.execute("sql", sql, args);
		rst.close();
	}
}
