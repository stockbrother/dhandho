package cc.dhandho.report.dupont;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.age5k.jcps.framework.handler.Handler2;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.graphdb.DbUtil;
import cc.dhandho.graphdb.OResultSetHandler;
import cc.dhandho.graphdb.dataver.DbUpgrader0_0_1;
import cc.dhandho.input.xueqiu.DateUtil;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.dupont.node.RoeNode;
import cc.dhandho.report.dupont.node.ValueNode;
import cc.dhandho.rest.server.DbProvider;

public class DupontAnalysis {
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

	public void execute(int year, DbProvider dbProvider) {
		Date reportDate = DateUtil.newDateOfYearLastDay(year, TimeZone.getDefault());

		dbProvider.executeWithDbSession(new Handler2<ODatabaseSession>() {

			@Override
			public void handle(ODatabaseSession t) {
				DbUtil.executeUpdate(t, "delete from " + DbUpgrader0_0_1.V_DUPONT_VNODE + " where reportDate=?",
						new Object[] { reportDate });
				Stream<String> corpIds = corpIdStream(t);
				corpIds.forEach(new Consumer<String>() {

					@Override
					public void accept(String corpId) {
						Context ac = execute(corpId, year);
						ValueNode top = ac.getValueNode();// ROE node
						// 3 component node:Netprofit-margin/Asset-turnover/Equity-multiplier.
						for (ValueNode vNode : top.getChildList()) {

							OVertex v = t.newVertex(DbUpgrader0_0_1.V_DUPONT_VNODE);
							v.setProperty("corpId", ac.corpId);
							v.setProperty("reportDate", reportDate);
							v.setProperty("define", vNode.getDefine().getClass().getName());
							v.setProperty("value", vNode.getValue());
							v.save();
						}
					}

				});

			}
		});

	}

}
