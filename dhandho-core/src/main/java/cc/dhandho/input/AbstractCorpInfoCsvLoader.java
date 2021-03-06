package cc.dhandho.input;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.OResultSetHandler;
import cc.dhandho.graphdb.dataver.DbUpgrader0_0_1;

public abstract class AbstractCorpInfoCsvLoader extends AbstractHeaderCsvFileHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractCorpInfoCsvLoader.class);

	protected static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	private static final String SQL = "select from " + DbUpgrader0_0_1.V_CORP_INFO + " where corpId=?";

	protected ODatabaseSession db;

	protected int rows;

	public AbstractCorpInfoCsvLoader(String name) {
		super(name);
	}

	public AbstractCorpInfoCsvLoader setDb(ODatabaseSession db) {
		this.db = db;
		return this;
	}

	@Override
	protected void handleRow(String[] next, Map<String, Integer> colIndexMap) {

		this.handleRowInternal(next, colIndexMap);
		rows++;

	}

	protected abstract void handleRowInternal(String[] next, Map<String, Integer> colIndexMap);

	protected OVertex getCorpInfoVertex(String x0) {
		return DbUtil.executeQuery(db, SQL, new Object[] { x0 }, new OResultSetHandler<OVertex>() {

			@Override
			public OVertex handle(OResultSet rst) {
				OVertex v = null;
				if (rst.hasNext()) {
					v = rst.next().getVertex().get();
				} else {
					v = db.newVertex(DbUpgrader0_0_1.V_CORP_INFO);
					v.setProperty("corpId", x0);
				}
				return v;
			}
		});

	}

}
