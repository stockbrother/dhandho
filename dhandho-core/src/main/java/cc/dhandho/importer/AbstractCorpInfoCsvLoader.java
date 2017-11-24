package cc.dhandho.importer;

import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.RtException;
import cc.dhandho.util.DbInitUtil;

public abstract class AbstractCorpInfoCsvLoader extends AbstractHeaderCsvFileHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractCorpInfoCsvLoader.class);

	protected static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	private static final String SQL = "select from " + DbInitUtil.V_CORP_INFO + " where corpId=?";

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
	public void execute(Reader freader) {
		this.db.begin();

		try {
			super.execute(freader);
			this.db.commit();
		} catch (Throwable t) {
			this.db.rollback();
			throw RtException.toRtException(t);
		}

	}

	@Override
	protected void handleRow(String[] next, Map<String, Integer> colIndexMap) {
		this.handleRowInternal(next, colIndexMap);
		rows++;
		if (rows % 100 == 0) {
			this.db.commit();
		}

	}

	protected abstract void handleRowInternal(String[] next, Map<String, Integer> colIndexMap);

	protected OVertex getCorpInfoVertex(String x0) {
		OResultSet rs = db.query(SQL, x0);

		OVertex v = null;
		if (rs.hasNext()) {
			v = rs.next().getVertex().get();
		} else {
			v = db.newVertex(DbInitUtil.V_CORP_INFO);
			v.setProperty("corpId", x0);
		}
		return v;
	}

}
