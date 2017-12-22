package cc.dhandho.util;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.AppContext;
import cc.dhandho.RtException;
import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.GDBResultSetProcessor;

public class DbInitUtil {

	public static String V_MASTER_REPORT = "MasterReport";

	public static String V_CORP_REPORT = "CorpReport";

	public static String V_CORP_INFO = "CorpInfo";
	
	public static String V_BALSHEET = "BALSHEET";
	
	public static String V_INCSTATEMENT = "INCSTATEMENT";
	
	public static String V_CFSTATEMENT = "CFSTATEMENT";
	

	private static final Logger LOG = LoggerFactory.getLogger(DbInitUtil.class);

	/**
	 * Create Schema If not done before on the DB provided.
	 * 
	 * @param app
	 */
	public static void initDb(AppContext app) {
		ODatabaseSession ds = app.openDB();

		try {
			createSchema(ds);

		} catch (Throwable e) {

			throw RtException.toRtException(e);
		}

	}

	public static OVertex getMetaInfo(ODatabaseSession db) {
		String className = "MetaInfo";
		OClass clazz = db.getClass(className);
		if (clazz == null) {
			return null;
		}

		return DbUtil.executeQuery(db, "select * from " + className + "", new Object[] {},
				new GDBResultSetProcessor<OVertex>() {

					@Override
					public OVertex process(OResultSet rst) {
						if (rst.hasNext()) {
							OResult rs = rst.next();
							Optional<OVertex> objO = rs.getVertex();
							return objO.get();
						}
						return null;
					}
				});

	}

	private static OVertex createMetaInfo(ODatabaseSession db) {
		String className = "MetaInfo";
		OClass clazz = db.createClass(className, "V");
		clazz.createProperty("dataVersion", OType.INTEGER);
		OVertex vertex = db.newVertex(clazz);
		vertex.setProperty("dataVersion", 0);
		vertex.save();
		return vertex;

	}

	private static void createSchema(ODatabaseSession db) {
		OVertex objO = getMetaInfo(db);
		if (objO == null) {
			objO = createMetaInfo(db);
		}

		int dataVersion = objO.getProperty("dataVersion");
		LOG.info("current data version:" + dataVersion);

		switch (dataVersion) {
		case 0:
			LOG.info("upgrade data version...");
			// Create Vertex
			OClass alias = db.createClass("AliasInfo", "V");
			alias.createProperty("columnIndex", OType.INTEGER);
			alias.createProperty("reportType", OType.STRING);
			alias.createProperty("aliasName", OType.STRING);

			OClass corp = db.createClass(V_CORP_INFO, "V");

			corp.createProperty("corpId", OType.STRING);
			corp.createProperty("corpName", OType.STRING);
			corp.createProperty("fullName", OType.STRING);
			corp.createProperty("category", OType.STRING);
			corp.createProperty("ipoDate", OType.DATE);
			corp.createProperty("province", OType.STRING);
			corp.createProperty("city", OType.STRING);
			corp.createProperty("webSite", OType.STRING);
			corp.createProperty("address", OType.STRING);

			OClass key = db.createClass("MetricMeta", "V");

			key.createProperty("reportType", OType.STRING);
			key.createProperty("metricName", OType.STRING);
			key.createProperty("columnIdx", OType.INTEGER);
			key.createIndex("Idx_MetricMeta_key", OClass.INDEX_TYPE.UNIQUE, "reportType", "metricName", "columnIdx");

			OClass report = db.createClass("CorpReport", "V");
			report.createProperty("corpId", OType.STRING);
			report.createProperty("reportDate", OType.DATE);

			OClass master = db.createClass(V_MASTER_REPORT, V_CORP_REPORT);
			master.createProperty("prev", OType.LINK);
			master.createProperty("next", OType.LINK);

			// Create Index
			master.createIndex("Idx_" + V_MASTER_REPORT + "_key", OClass.INDEX_TYPE.UNIQUE, "corpId", "reportDate");

			// Save version info
			objO.setProperty("dataVersion", 1);
			objO.save();
			LOG.info("data version now is :" + 1);
			break;
		case 1:
			LOG.info("data version is 1,no need to upgrade");
			break;
		default:
			throw new RuntimeException("todo");
		}
	}

}
