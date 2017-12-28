package cc.dhandho.importer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;

import cc.dhandho.DbAliasInfos;
import cc.dhandho.Processor;
import cc.dhandho.Quarter;
import cc.dhandho.RtException;
import cc.dhandho.server.DbProvider;
import cc.dhandho.util.DbInitUtil;

/**
 * The 1st stage before loading data into db. This loader scan the specified
 * folder and collect all metric keys. And create meta info for them.
 * 
 * @author Wu
 *
 */
public class GDBWashedFileSchemaLoader extends QuarterWahsedFileLoader {

	private static Logger LOG = LoggerFactory.getLogger(GDBWashedFileSchemaLoader.class);

	DbProvider appContext;

	ODatabaseSession session;

	// store type=>aliasList.
	private Map<String, List<String>> propertyListMap = new HashMap<>();
	// store type=>columnIndexList
	private Map<String, List<Integer>> propertyListMap2 = new HashMap<>();

	public GDBWashedFileSchemaLoader(DbProvider dbc, FileObject dir, Quarter quarter) {
		super(dir, quarter);

		this.appContext = dbc;
	}

	public GDBWashedFileSchemaLoader limit(int limit) {
		this.limit = limit;
		return this;
	}

	@Override
	public void start() throws IOException {
		// @see #onTableData();
		super.start();
		DbAliasInfos ais = new DbAliasInfos();

		this.appContext.executeWithDbSession(new Processor<ODatabaseSession>() {

			@Override
			public void process(ODatabaseSession db) {
				ais.initialize(db);
				for (Map.Entry<String, List<String>> entry : propertyListMap.entrySet()) {
					ais.getOrCreateColumnIndexByAliasList(db, entry.getKey(), entry.getValue());
				}

			}
		});

		this.appContext.executeWithDbSession(new Processor<ODatabaseSession>() {

			@Override
			public void process(ODatabaseSession db) {
				doProcess(ais, db);
			}
		});
	}

	private void doProcess(DbAliasInfos ais, ODatabaseSession db) {
		this.session = db;

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
			this.session = null;
		}
	}

	@Override
	protected int onTableData(String type, FileObject file, int number, CsvHeaderRowMap headers, CsvRowMap body)
			throws IOException {
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
