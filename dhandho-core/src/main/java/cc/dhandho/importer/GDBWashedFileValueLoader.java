package cc.dhandho.importer;

import java.io.File;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.AppContext;
import cc.dhandho.DbAliasInfos;
import cc.dhandho.Quarter;
import cc.dhandho.RtException;
import cc.dhandho.util.DbInitUtil;
import cc.dhandho.xueqiu.DateUtil;

public class GDBWashedFileValueLoader extends WashedFileLoader {

	AppContext appContext;

	ODatabaseSession session;

	DbAliasInfos aliasInfos;

	public GDBWashedFileValueLoader(AppContext appContext, File dir, Quarter quarter) {
		super(dir, quarter);
		this.appContext = appContext;
	}

	public GDBWashedFileValueLoader limit(int limit) {
		this.limit = limit;
		return this;
	}

	@Override
	public void start() {
		this.session = this.appContext.openDB();
		this.aliasInfos = new DbAliasInfos();
		try {
			this.aliasInfos.initialize(this.session, this.appContext.getDbTemplate());
			super.start();

		} finally {
			this.session.close();
			this.session = null;
		}
	}

	@Override
	protected void onReader(String type, File file, Reader freader, int number) {
		this.session.begin();
		try {
			super.onReader(type, file, freader, number);
			this.session.commit();
		} catch (Throwable t) {			
			this.session.rollback();
			throw RtException.toRtException(t);
		} 
	}

	@Override
	protected void onRowData(String reportType, String corpId, Date reportDate, List<String> keyList,
			List<BigDecimal> valueList) {
		OClass reportClazz = this.session.getClass("CorpReport");

		OResultSet rs = this.session.query("select from " + reportType + " where corpId = ? and reportDate = ?", corpId,
				reportDate);

		OVertex row = null;
		if (rs.hasNext()) {
			row = rs.next().getVertex().get();
		} else {
			row = this.session.newVertex(reportType);
			row.setProperty("corpId", corpId);
			row.setProperty("reportDate", reportDate);
			// link from threeReport

		}
		for (int i = 0; i < keyList.size(); i++) {
			String key = keyList.get(i);
			Integer cidx = aliasInfos.getColumnIndexByAlias(reportType, key);

			BigDecimal value = valueList.get(i);
			Double dvalue = null;
			if (value != null) {
				dvalue = value.doubleValue();
			}
			// row.setProperty("d_" + i, dvalue);
			String name = "d_" + cidx;
			row.setProperty(name, dvalue);

		}

		row.save();
		ORID oid = row.getIdentity();

		OVertex master = this.getOrCreateMasterReport(corpId, reportDate);
		master.setProperty(reportType.toLowerCase(), oid);
		master.save();
	}

	protected OVertex getMasterReport(String corpId, Date reportDate) {
		OResultSet rs = this.session.query(
				"select from " + DbInitUtil.V_MASTER_REPORT + " where corpId = ? and reportDate = ?", corpId,
				reportDate);

		OVertex row = null;
		if (rs.hasNext()) {
			row = rs.next().getVertex().get();
			return row;
		} else {
			return null;
		}
	}

	protected OVertex getOrCreateMasterReport(String corpId, Date reportDate) {

		OVertex row = getMasterReport(corpId, reportDate);

		if (row == null) {
			Date prevDate = DateUtil.prevDate(reportDate, TimeZone.getDefault());
			Date nextDate = DateUtil.nextDate(reportDate, TimeZone.getDefault());

			OVertex prev = getMasterReport(corpId, prevDate);
			OVertex next = getMasterReport(corpId, nextDate);

			row = this.session.newVertex(DbInitUtil.V_MASTER_REPORT);
			row.setProperty("corpId", corpId);
			row.setProperty("reportDate", reportDate);
			row.save();
			if (prev != null) {
				row.setProperty("prev", prev.getIdentity());
				prev.setProperty("next", row.getIdentity());
				prev.save();
			}
			if (next != null) {
				row.setProperty("next", next.getIdentity());
				next.setProperty("prev", row.getIdentity());
				next.save();
			}

		}
		return row;
	}

}
