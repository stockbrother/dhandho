package cc.dhandho.graphdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

public class DbUtil {

	public static interface DbCallback {
		public void execute(ODatabaseSession db);
	}

	public static <T> T executeQuery(ODatabaseSession session, String sql, GDBResultSetProcessor<T> resultProcessor) {
		return executeQuery(session, sql, new Object[] {}, resultProcessor);
	}

	public static <T> T executeQuery(ODatabaseSession session, String sql, Object[] args,
			GDBResultSetProcessor<T> resultProcessor) {
		OResultSet rst = session.query(sql, args);
		try {
			T rt = resultProcessor.process(rst);
			return rt;
		} finally {
			rst.close();
		}

	}
}
