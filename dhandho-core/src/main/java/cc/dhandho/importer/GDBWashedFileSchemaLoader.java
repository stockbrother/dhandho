package cc.dhandho.importer;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;

import cc.dhandho.AppContext;
import cc.dhandho.DbAliasInfos;
import cc.dhandho.Quarter;
import cc.dhandho.RtException;
import cc.dhandho.graphdb.GDBTemplate;
import cc.dhandho.util.DbInitUtil;

/**
 * The 1st stage before loading data into db. This loader scan the specified
 * folder and collect all metric keys. And create meta info for them.
 * 
 * @author Wu
 *
 */
public class GDBWashedFileSchemaLoader extends WashedFileLoader {

	private static Logger LOG = LoggerFactory.getLogger(GDBWashedFileSchemaLoader.class);

	AppContext appContext;

	ODatabaseSession session;

	// store type=>aliasList.
	private Map<String, List<String>> propertyListMap = new HashMap<>();
	// store type=>columnIndexList
	private Map<String, List<Integer>> propertyListMap2 = new HashMap<>();

	public GDBWashedFileSchemaLoader(AppContext appContext, File dir, Quarter quarter) {
		super(dir, quarter);

		this.appContext = appContext;
	}

	public GDBWashedFileSchemaLoader limit(int limit) {
		this.limit = limit;
		return this;
	}

	@Override
	public void start() {
		// @see #onTableData();
		super.start();
		DbAliasInfos ais = new DbAliasInfos();

		ODatabaseSession db = this.appContext.openDB();

		GDBTemplate t = this.appContext.getDbTemplate();
		ais.initialize(db, t);
		for (Map.Entry<String, List<String>> entry : propertyListMap.entrySet()) {
			ais.getOrCreateColumnIndexByAliasList(db, t, entry.getKey(), entry.getValue());
		}

		this.session = this.appContext.openDB();

		try {
			for (Map.Entry<String, List<String>> entry : propertyListMap.entrySet()) {
				String type = entry.getKey();
				type = type.toUpperCase();

				OClass reportClazz = this.session.getClass(type);

				if (reportClazz == null) {
					reportClazz = this.session.createClass(type, "CorpReport");
					// add link property for masterReport
					OClass masterClazz = this.session.getClass(DbInitUtil.V_MASTER_REPORT);
					masterClazz.createProperty(type.toLowerCase(), OType.LINK);// lower case as property of link.

					reportClazz.createIndex("Idx_" + type + "_key", OClass.INDEX_TYPE.UNIQUE, "corpId", "reportDate");
				}

				List<String> set = entry.getValue();
				for (String key : set) {
					Integer columnIndex = ais.getColumnIndexByAlias(type, key);
					String name = "d_" + columnIndex;
					OProperty property = reportClazz.getProperty(name);
					if (property == null) {
						reportClazz.createProperty(name, OType.DOUBLE);//
						LOG.info("create property:" + reportClazz.getName() + "." + name + " for alias:" + key);
					}
				}
			}

		} finally {
			this.session.close();
			this.session = null;
		}
	}

	@Override
	protected int onTableData(String type, File file, int number, CsvHeaderRowMap headers, CsvRowMap body) {
		List<String> set = propertyListMap.get(type);

		if (set == null) {
			set = new ArrayList<>();
			propertyListMap.put(type, set);
		}
		for (String key : body.keyList) {
			if (!set.contains(key)) {
				set.add(key);
			}
		}

		return body.keyList.size();
	}

	@Override
	protected void onRowData(String reportType, String corpId, Date reportDate, List<String> keyList,
			List<BigDecimal> valueList) {
		throw new RtException("not here");
	}

}
