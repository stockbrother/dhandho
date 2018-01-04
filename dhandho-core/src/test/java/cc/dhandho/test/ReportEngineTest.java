package cc.dhandho.test;

import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.container.impl.ContainerImpl;

import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.DhoDataHome;
import cc.dhandho.ReportMetaInfos;
import cc.dhandho.graphdb.MyDataUpgraders;
import cc.dhandho.input.loader.CorpInfoInputDataLoader;
import cc.dhandho.input.loader.WashedInputDataLoader;
import cc.dhandho.report.CorpDatedMetricReportData;
import cc.dhandho.report.MetricDefines;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.impl.ReportEngineImpl;
import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class ReportEngineTest extends TestCase {

	public void test() {

		Container app = new ContainerImpl();
		DbProvider dbProvider = TestUtil.newInMemoryTestDbProvider(true);
		dbProvider.executeWithDbSession(new MyDataUpgraders());

		DbReportMetaInfos metaInfos = new DbReportMetaInfos();
		dbProvider.executeWithDbSession(metaInfos.initializer());

		MetricDefines metricDefines = TestUtil.newMetricDefines();

		app.addComponent(DhoDataHome.class, TestUtil.getHome());
		app.addComponent(DbProvider.class, dbProvider);
		app.addComponent(ReportMetaInfos.class, metaInfos);
		app.addComponent(MetricDefines.class, metricDefines);

		//

		CorpInfoInputDataLoader dbu = app.newInstance(CorpInfoInputDataLoader.class);
		dbProvider.executeWithDbSession(dbu);
		// load washed data to DB.
		WashedInputDataLoader wdu = app.newInstance(WashedInputDataLoader.class);
		dbProvider.executeWithDbSession(wdu);

		ReportEngine re = app.newInstance(ReportEngineImpl.class);
		String corpId = "000002";
		int year = 2016;
		String metric = "资产总计";
		Double d = re.getMetricValue(corpId, year, metric);
		System.out.println(d);

		int[] years = new int[] { 2016, 2015 };
		String[] metrics = new String[] { "资产总计", "净利润" };
		CorpDatedMetricReportData data = re.getReport(corpId, years, metrics);

		TestCase.assertNotNull("", data);

		System.out.println(data.print(new StringBuffer()));

	}
}
