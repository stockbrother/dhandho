package cc.dhandho.test.report;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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
import cc.dhandho.report.dupont.DupontAnalysis;
import cc.dhandho.report.dupont.PointsFilterDupontSvgBuilder;
import cc.dhandho.report.dupont.node.AssetTurnover;
import cc.dhandho.report.dupont.node.EquityMultiplier;
import cc.dhandho.report.dupont.node.ProfitMarginNode;
import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.test.util.TestUtil;
import cc.dhandho.util.SvgUtil;
import junit.framework.TestCase;

public class DupontAnalysisStoreTest extends TestCase {
	static final double delta = 0.00001;
	String corpId = "000100";
	int year = 2016;

	double netProfit = 2137540000D;// 净利润
	double revenue = 106617858000D;// 营业总收入
	double totalAsset = 147136786000D;// 资产总计
	double equity = 45746781000D;// 所有者权益_或股东权益_合计

	double profitMargin = netProfit / revenue;
	double assertTurnover = revenue / totalAsset;
	double equityMultiplier = totalAsset / equity;

	DbProvider dbProvider;
	ReportEngine reportEngine;
	DupontAnalysis dupontAnalysis;

	@Override
	protected void setUp() throws Exception {
		dbProvider = TestUtil.newInMemoryTestDbProvider(true);
		reportEngine = TestUtil.newInMemoryReportEgine(dbProvider);

		dupontAnalysis = new DupontAnalysis(reportEngine);

	}

	@Override
	protected void tearDown() throws Exception {

	}

	public void testDupontAnalysisStoreAndSvg() throws Exception {

		dupontAnalysis.analysisAndStore(year, dbProvider);
		Date reportDate = DateUtil.newDateOfYearLastDay(year, TimeZone.getDefault());
		Map<String, Map<String, OVertex>> vNodeMapMap = new HashMap<>();

		// find the result from DB.
		this.dbProvider.executeWithDbSession(new Handler2<ODatabaseSession>() {

			@Override
			public void handle(ODatabaseSession t) {
				String sql = "select from " + DbUpgrader0_0_1.V_DUPONT_VNODE + " where reportDate=?";

				DbUtil.executeQuery(t, sql, new Object[] { reportDate }, new OResultSetHandler<Void>() {

					@Override
					public Void handle(OResultSet rs) {
						TestCase.assertTrue("empty result, no value vode genereated.", rs.hasNext());
						while (rs.hasNext()) {
							OResult rI = rs.next();
							OVertex vI = rI.getVertex().get();
							String corpId = vI.getProperty("corpId");
							String define = vI.getProperty("define");

							Map<String, OVertex> map = vNodeMapMap.get(corpId);
							if (map == null) {
								map = new HashMap<>();
								vNodeMapMap.put(corpId, map);
							}
							map.put(define, vI);
						}
						return null;
					}
				});
			}
		});
		Map<String, OVertex> vNodeMap = vNodeMapMap.get(corpId);
		TestCase.assertNotNull(vNodeMap);
		OVertex profitMarginV = vNodeMap.get(ProfitMarginNode.class.getName());
		OVertex assertTurnoverV = vNodeMap.get(AssetTurnover.class.getName());
		OVertex equityMultiplierV = vNodeMap.get(EquityMultiplier.class.getName());

		TestCase.assertNotNull("", profitMarginV);
		TestCase.assertNotNull("", assertTurnoverV);
		TestCase.assertNotNull("", equityMultiplierV);

		// TestCase.assertEquals(expected, actual);
		Double profitMarginA = profitMarginV.getProperty("value");
		Double assertTurnoverA = assertTurnoverV.getProperty("value");
		Double equityMultiplierA = equityMultiplierV.getProperty("value");

		TestCase.assertNotNull(profitMarginA);
		TestCase.assertNotNull(assertTurnoverA);
		TestCase.assertNotNull(equityMultiplierA);

		TestCase.assertEquals(profitMargin, profitMarginA.doubleValue(), delta);
		TestCase.assertEquals(assertTurnover, assertTurnoverA.doubleValue(), delta);
		TestCase.assertEquals(equityMultiplier, equityMultiplierA.doubleValue(), delta);

		//
		doTestSvg(ProfitMarginNode.class.getName(), AssetTurnover.class.getName());
	}

	private void doTestSvg(String xDefine, String yDefine) {

		SvgChartWriter writer = new SvgChartWriter();
		StringBuilder sb = new StringBuilder();
		writer.writeScatterSvg(new PointsFilterDupontSvgBuilder(year, new String[] { xDefine, yDefine }, dbProvider)
				.centerCorpId(corpId)//
				.filter(1D)//
				, xDefine, yDefine, new String[] { corpId }, sb);

		sb = SvgUtil.writeSvg2Html(600, 320, sb.toString(), new StringBuilder());
		System.out.println(sb);

	}
}
