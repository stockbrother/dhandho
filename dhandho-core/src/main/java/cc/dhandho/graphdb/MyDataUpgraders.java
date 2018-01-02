package cc.dhandho.graphdb;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.age5k.jcps.dataversion.DataVersion;
import com.age5k.jcps.dataversion.VersionUpgraders;
import com.age5k.jcps.framework.handler.Handler3;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.graphdb.dataver.DbUpgrader0_0_1;
import cc.dhandho.rest.server.DbUpgradeContext;

public class MyDataUpgraders extends VersionUpgraders<DbUpgradeContext>
		implements Handler3<ODatabaseSession, DbUpgradeContext> {

	private static Map<Integer, Map<Integer, Map<Integer, DataVersion>>> versionMap = new HashMap<>();

	// unknown means not resolved yet.
	public static final DataVersion V_UNKNOW = DataVersion.valueOf(0, 0, -1);

	public static final DataVersion V_0_0_0 = DataVersion.valueOf(0, 0, 0);

	// v0.0.1,
	public static final DataVersion V_0_0_1 = DataVersion.valueOf(0, 0, 1);

	// v0.0.2,
	public static final DataVersion V_0_0_2 = DataVersion.valueOf(0, 0, 2);

	// v0.0.3,
	public static final DataVersion V_0_0_3 = DataVersion.valueOf(0, 0, 3);

	public static final DataVersion V_0_0_4 = DataVersion.valueOf(0, 0, 4);

	public static final DataVersion V_0_0_5 = DataVersion.valueOf(0, 0, 5);

	public static final DataVersion V_0_0_6 = DataVersion.valueOf(0, 0, 6);

	public static final DataVersion V_0_0_7 = DataVersion.valueOf(0, 0, 7);

	public static final DataVersion V_0_0_8 = DataVersion.valueOf(0, 0, 8);

	public static final DataVersion V_0_0_9 = DataVersion.valueOf(0, 0, 9);

	public static final DataVersion V_0_0_10 = DataVersion.valueOf(0, 0, 10);

	public static final DataVersion V_0_0_11 = DataVersion.valueOf(0, 0, 11);

	public static final DataVersion V_0_0_12 = DataVersion.valueOf(0, 0, 12);

	public static final DataVersion V_0_0_13 = DataVersion.valueOf(0, 0, 13);

	public MyDataUpgraders() {
		super(V_0_0_1);
		this.add(new DbUpgrader0_0_1());

	}

	@Override
	protected DataVersion resolveDataVersion(DbUpgradeContext arg0) {
		OVertex ver = getMetaInfo(arg0.getDbs());
		if (ver == null) {
			createMetaInfo(arg0.getDbs(), V_0_0_0);
			return V_0_0_0;
		}
		String vS = ver.getProperty("dataVersion");
		return DataVersion.valueOf(vS);
	}

	@Override
	protected void writeDataVersion(DbUpgradeContext arg0, DataVersion arg1) {
		OVertex ver = getMetaInfo(arg0.getDbs());
		ver.setProperty("dataVersion", arg1.toString());
		ver.save();
	}

	public static OVertex getMetaInfo(ODatabaseSession db) {
		String className = "MetaInfo";
		OClass clazz = db.getClass(className);
		if (clazz == null) {
			return null;
		}

		return DbUtil.executeQuery(db, "select * from " + className + "", new Object[] {},
				new OResultSetHandler<OVertex>() {

					@Override
					public OVertex handle(OResultSet rst) {
						if (rst.hasNext()) {
							OResult rs = rst.next();
							Optional<OVertex> objO = rs.getVertex();
							return objO.get();
						}
						return null;
					}
				});

	}

	private static OVertex createMetaInfo(ODatabaseSession db, DataVersion dver) {
		String className = "MetaInfo";
		OClass clazz = db.createClass(className, "V");
		clazz.createProperty("dataVersion", OType.STRING);
		OVertex vertex = db.newVertex(clazz);
		vertex.setProperty("dataVersion", dver.toString());
		vertex.save();
		return vertex;

	}

	@Override
	public DbUpgradeContext handle(ODatabaseSession req) {
		DbUpgradeContext rt = new DbUpgradeContext(req);
		this.upgrade(rt);
		return rt;
	}

}
