package cc.dhandho.graphdb.handler;

import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.graphdb.OResultSetHandler;
import cc.dhandho.graphdb.dataver.DbUpgrader0_0_1;

public class CorpNameQueryHandler extends DbQueryHandler<String> {
	private static class RSH implements OResultSetHandler<String> {

		@Override
		public String handle(OResultSet arg0) {
			//
			if (arg0.hasNext()) {
				OResult or = arg0.next();
				String rt = or.getProperty("corpName");
				return rt;
			} else {
				return null;
			}
		}

	}

	static final String sql = "select corpName from " + DbUpgrader0_0_1.V_CORP_INFO + " where corpId=?";
	static final RSH H = new RSH();

	public CorpNameQueryHandler(String corpId) {
		super(sql, H);
		this.args = new Object[] { corpId };
	}

}
