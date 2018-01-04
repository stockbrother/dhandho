package cc.dhandho.test.report;

import java.util.List;

import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.dupont.DupontAnalysis;
import cc.dhandho.report.dupont.DupontAnalysis.Context;
import cc.dhandho.report.dupont.node.AssetTurnover;
import cc.dhandho.report.dupont.node.DefineNode;
import cc.dhandho.report.dupont.node.EquityMultiplier;
import cc.dhandho.report.dupont.node.ProfitMarginNode;
import cc.dhandho.report.dupont.node.ValueNode;
import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class DupontAnalysisTest extends TestCase {
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
	
	int setUp = 0;
			
	@Override
	protected void setUp() throws Exception {
		dbProvider = TestUtil.newInMemoryTestDbProvider(true);
		reportEngine = TestUtil.newInMemoryReportEgine(dbProvider);
		dupontAnalysis = new DupontAnalysis(reportEngine);
	}

	@Override
	protected void tearDown() throws Exception {

	}

	public void testDupontAnalysisSimple() throws Exception {

		Context ac = dupontAnalysis.execute(new Context(corpId, 2016));
		ValueNode vNode = ac.getValueNode();
		TestCase.assertNotNull("", vNode);
		if (true) {

			System.out.println("profitMargin:" + profitMargin);
			System.out.println("assertTurnover:" + assertTurnover);
			System.out.println("equityMultiplier:" + equityMultiplier);
			StringBuilder sb = vNode.toJson(new StringBuilder());
			System.out.println(sb);
		}

		List<ValueNode> childL1 = vNode.getChildList();
		TestCase.assertEquals(3, childL1.size());
		{
			ValueNode child = childL1.get(0);
			TestCase.assertNotNull("", child);
			DefineNode define = child.getDefine();
			TestCase.assertEquals(ProfitMarginNode.class, define.getClass());
			double value = child.getValue();
			TestCase.assertEquals(profitMargin, value, delta);
		}
		{

			ValueNode child = childL1.get(1);
			TestCase.assertNotNull("", child);
			DefineNode define = child.getDefine();
			TestCase.assertEquals(AssetTurnover.class, define.getClass());
			double value = child.getValue();
			TestCase.assertEquals(assertTurnover, value, delta);
		}
		{

			ValueNode child = childL1.get(2);
			DefineNode define = child.getDefine();
			TestCase.assertNotNull("", child);
			TestCase.assertEquals(EquityMultiplier.class, define.getClass());
			double value = child.getValue();
			TestCase.assertEquals(equityMultiplier, value, delta);
		}

	}

}
