package cc.dhandho.graphdb.dataver;

import com.age5k.jcps.dataversion.DataVersion;
import com.age5k.jcps.dataversion.VersionUpgrader;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;

import cc.dhandho.rest.server.DbUpgradeContext;

public class DbUpgrader0_0_1 extends VersionUpgrader<DbUpgradeContext> {
	public static String V_MASTER_REPORT = "MasterReport";

	public static String V_CORP_REPORT = "CorpReport";//CorpReport

	public static String V_CORP_INFO = "CorpInfo";

	public static String V_BALSHEET = "BALSHEET";

	public static String V_INCSTATEMENT = "INCSTATEMENT";

	public static String V_CFSTATEMENT = "CFSTATEMENT";
	
	public static String V_DUPONT_DNODE = "DupontDNode";
	
	public static String V_DUPONT_VNODE = "DupontVNode";
	

	public DbUpgrader0_0_1() {
		super(DataVersion.valueOf(0, 0, 0), DataVersion.valueOf(0, 0, 1));
	}

	@Override
	public void doUpgrade(DbUpgradeContext arg0) {
		ODatabaseSession db = arg0.getDbs();

		// Create Vertex
		OClass alias = db.createClass("AliasInfo", "V");
		alias.createProperty("columnIndex", OType.INTEGER);
		alias.createProperty("reportType", OType.STRING);
		alias.createProperty("aliasName", OType.STRING);

		// Corps
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

		//
//		OClass key = db.createClass("MetricMeta", "V");
//
//		key.createProperty("reportType", OType.STRING);
//		key.createProperty("metricName", OType.STRING);
//		key.createProperty("columnIdx", OType.INTEGER);
//		key.createIndex("Idx_MetricMeta_key", OClass.INDEX_TYPE.UNIQUE, "reportType", "metricName", "columnIdx");

		OClass report = db.createClass(V_CORP_REPORT, "V");
		report.createProperty("corpId", OType.STRING);
		report.createProperty("reportDate", OType.DATE);

		OClass master = db.createClass(V_MASTER_REPORT, V_CORP_REPORT);
		master.createProperty("prev", OType.LINK);
		master.createProperty("next", OType.LINK);

		// Create Index
		master.createIndex("Idx_" + V_MASTER_REPORT + "_key", OClass.INDEX_TYPE.UNIQUE, "corpId", "reportDate");
		
		// Dupont Analysis
		OClass dupontD = db.createClass(V_DUPONT_VNODE, "V");
		dupontD.createProperty("name", OType.STRING);
				
		OClass dupontV = db.createClass(V_DUPONT_VNODE, "V");
		dupontV.createProperty("corpId", OType.STRING);
		dupontV.createProperty("define", OType.LINK);
		dupontV.createProperty("reportDate", OType.DATE);
		
	}

}
