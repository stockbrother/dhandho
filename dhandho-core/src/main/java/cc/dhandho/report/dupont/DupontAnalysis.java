package cc.dhandho.report.dupont;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.stream.Stream;

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

		dbProvider.executeWithDbSession(new Handler2<ODatabaseSession>() {

			@Override
			public void handle(ODatabaseSession t) {
				DbUtil.executeUpdate(t, "delete from " + DbUpgrader0_0_1.V_DUPONT_VNODE + " where reportDate=?",
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
						}
					}

				});

			}
		});

	}

	/**
	 * Build a svg 2D point chart based on the value node for all corpIds.
	 * 
	 * @param xDefine
	 * @param yDefine
	 * @param year
	 */
	public <X extends DefineNode, Y extends DefineNode> StringBuilder buildScatterSvg(Class<X> xDefine,
			Class<Y> yDefine, int year, String[] heighLightCorpId, DbProvider dbProvider, StringBuilder sb) {
		Date reportDate = DateUtil.newDateOfYearLastDay(year, TimeZone.getDefault());
		String typeX = xDefine.getName();
		String typeY = yDefine.getName();
		
		String xLabel = xDefine.getSimpleName();
		String yLabel = yDefine.getSimpleName();
		
		dbProvider.executeWithDbSession(new Handler2<ODatabaseSession>() {

			@Override
			public void handle(ODatabaseSession t) {

				DbUtil.executeQuery(t,
						"select from " + DbUpgrader0_0_1.V_DUPONT_VNODE
								+ " where reportDate = ? and (define = ? or define = ?)",
						new Object[] { reportDate, typeX, typeY }, new OResultSetHandler<StringBuilder>() {

							@Override
							public StringBuilder handle(OResultSet arg0) {
								Map<String, Double[]> pointMap = new HashMap<>();
								while (arg0.hasNext()) {
									OResult rI = arg0.next();
									OVertex vI = rI.getVertex().get();
									String corpId = vI.getProperty("corpId");
									String type = vI.getProperty("define");
									Double value = vI.getProperty("value");
									Double[] point = pointMap.get(corpId);
									if (point == null) {
										point = new Double[2];
										pointMap.put(corpId, point);
									}
									if (typeX.equals(type)) {
										point[0] = value;
									} else if (typeY.equals(type)) {
										point[1] = value;
									} else {
										throw new JcpsException("type mismatch:" + type);
									}
								}
								SvgChartWriter writer = new SvgChartWriter();

								writer.writeScatterSvg(xLabel, yLabel, pointMap, heighLightCorpId, sb);

								return null;
							}
						});
			}
		});
		return sb;
	}

}
