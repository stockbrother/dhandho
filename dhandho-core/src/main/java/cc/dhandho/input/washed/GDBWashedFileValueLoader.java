package cc.dhandho.input.washed;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.vfs2.FileObject;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.Quarter;
import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.handler.Handler2;

import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.OResultSetHandler;
import cc.dhandho.graphdb.dataver.DbUpgrader0_0_1;
import cc.dhandho.input.xueqiu.DateUtil;
import cc.dhandho.rest.server.DbProvider;

public class GDBWashedFileValueLoader extends QuarterWahsedFileLoader {

	DbProvider dbProvider;

	ODatabaseSession session;

	DbReportMetaInfos aliasInfos;

	private int files;

	public GDBWashedFileValueLoader(DbProvider appContext, FileObject dir, Quarter quarter) {
		super(dir, quarter);
		this.dbProvider = appContext;
	}

	public GDBWashedFileValueLoader limit(int limit) {
		this.limit = limit;
		return this;
	}

	@Override
	public void start() throws IOException {
		this.dbProvider.executeWithDbSession(new Handler2<ODatabaseSession>() {

			@Override
			public void handle(ODatabaseSession db) {
				try {
					doProcess(db);
				} catch (IOException e) {
					throw JcpsException.toRtException(e);
				}
			}
		});
	}

	private void doProcess(ODatabaseSession db) throws IOException {
		this.session = db;
		OIntentMassiveInsert intent = new OIntentMassiveInsert();
		// intent.setEnableCache(false);
		this.session.declareIntent(intent);

		this.aliasInfos = new DbReportMetaInfos();
		try {
			this.aliasInfos.initialize(this.session);
			super.start();

		} finally {
			this.session = null;
		}
	}

	@Override
	protected void onReader(String type, FileObject file, Reader freader, int number) throws IOException {
		super.onReader(type, file, freader, number);

		files++;

	}

	@Override
	protected void onRowData(String reportType, String corpId, Date reportDate, List<String> keyList,
			List<BigDecimal> valueList) {

		String sql = "select from " + reportType + " where corpId = ? and reportDate = ?";
		DbUtil.executeQuery(session, sql, new Object[] { corpId, reportDate }, new OResultSetHandler<Void>() {

			@Override
			public Void handle(OResultSet rst) {
				OVertex row = null;
				if (rst.hasNext()) {
					row = rst.next().getVertex().get();
				} else {
					row = session.newVertex(reportType);
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

				OVertex master = getOrCreateMasterReport(corpId, reportDate);
				master.setProperty(reportType.toLowerCase(), oid);
				master.save();
				return null;
			}
		});

	}

	protected OVertex getMasterReport(String corpId, Date reportDate) {

		String sql = "select from " + DbUpgrader0_0_1.V_MASTER_REPORT + " where corpId = ? and reportDate = ?";
		return DbUtil.executeQuery(session, sql, new Object[] { corpId, reportDate },
				new OResultSetHandler<OVertex>() {

					@Override
					public OVertex handle(OResultSet rst) {
						OVertex row = null;
						if (rst.hasNext()) {
							row = rst.next().getVertex().get();
							return row;
						} else {
							return null;
						}
					}
				});

	}

	protected OVertex getOrCreateMasterReport(String corpId, Date reportDate) {

		OVertex row = getMasterReport(corpId, reportDate);

		if (row == null) {
			Date prevDate = DateUtil.prevDate(reportDate, TimeZone.getDefault());
			Date nextDate = DateUtil.nextDate(reportDate, TimeZone.getDefault());

			OVertex prev = getMasterReport(corpId, prevDate);
			OVertex next = getMasterReport(corpId, nextDate);

			row = this.session.newVertex(DbUpgrader0_0_1.V_MASTER_REPORT);
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
