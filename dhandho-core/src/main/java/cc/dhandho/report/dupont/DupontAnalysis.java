package cc.dhandho.report.dupont;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.handler.Handler2;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.OResultSetHandler;
import cc.dhandho.graphdb.dataver.DbUpgrader0_0_1;
import cc.dhandho.input.xueqiu.DateUtil;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.chart.SvgChartWriter;
import cc.dhandho.report.dupont.node.DefineNode;
import cc.dhandho.report.dupont.node.RoeNode;
import cc.dhandho.report.dupont.node.ValueNode;
import cc.dhandho.rest.server.DbProvider;

public class DupontAnalysis {
	private static final Logger LOG = LoggerFactory.getLogger(DupontAnalysis.class);

	public static class Context {

		private String corpId;

		private int year;

		// The root node of the result tree.
		private ValueNode valueNode;

		public Context(String corpId, int year) {
			this.corpId = corpId;
			this.year = year;
		}

		public ValueNode getValueNode() {
			return valueNode;
		}

		public String getCorpId() {
			return corpId;
		}

		public int getYear() {
			return year;
		}

		public void setValueNode(ValueNode vNode) {
			this.valueNode = vNode;
		}
	}

	private ReportEngine reportEngine;

	private RoeNode roeNode;

	public DupontAnalysis(ReportEngine re) {
		this.reportEngine = re;
		this.roeNode = new RoeNode(this);
	}

	public ReportEngine getReportEngine() {
		return reportEngine;
	}

	public Context execute(String corpId, int year) {
		return execute(new Context(corpId, year));
	}

	public Context execute(Context ac) {

		ValueNode vNode = roeNode.calculate(ac);
		ac.setValueNode(vNode);

		return ac;
	}

	private Stream<String> corpIdStream(ODatabaseSession db) {
		String sql = "select corpId from " + DbUpgrader0_0_1.V_CORP_INFO;
		return DbUtil.executeQuery(db, sql, new OResultSetHandler<List<String>>() {

			@Override
			public List<String> handle(OResultSet req) {
				List<String> rt = new ArrayList<>();
				while (req.hasNext()) {
					String corpId = req.next().getProperty("corpId");
					rt.add(corpId);
				}
				return rt;
			}
		}).stream();
	}

	/**
	 * Execute analysis on all corps with the year specified.
	 * 
	 * @param year
	 * @param dbProvider
	 */
	public void analysisAndStore(int year, DbProvider dbProvider) {
		Date reportDate = DateUtil.newDateOfYearLastDay(year, TimeZone.getDefault());
		LOG.info("goint to do dupont analysis for report date:" + reportDate);

		dbProvider.executeWithDbSession(new Handler2<ODatabaseSession>() {

			@Override
			public void handle(ODatabaseSession t) {
				// TODO make sure unsafe means?
				DbUtil.executeUpdate(t, "delete from " + DbUpgrader0_0_1.V_DUPONT_VNODE + " where reportDate=? unsafe",
						new Object[] { reportDate });

				Stream<String> corpIds = corpIdStream(t);
				corpIds.forEach(new Consumer<String>() {

					@Override
					public void accept(String corpId) {
						Context ac = DupontAnalysis.this.execute(corpId, year);
						ValueNode top = ac.getValueNode();// ROE node

						// 3 component node:Netprofit-margin/Asset-turnover/Equity-multiplier.

						for (ValueNode vNode : top.getChildList()) {
							OVertex v = t.newVertex(DbUpgrader0_0_1.V_DUPONT_VNODE);
							v.setProperty("corpId", ac.corpId);
							v.setProperty("reportDate", reportDate);
							v.setProperty("define", vNode.getDefine().getClass().getName());
							v.setProperty("value", vNode.getValue());
							v.save();
							LOG.info("save vnode:" + v.toJSON());
						}
					}

				});

			}
		});

		LOG.info("end of dupont analysis for report date:" + reportDate);
	}

}
