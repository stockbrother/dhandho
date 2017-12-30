package cc.dhandho.test;

import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.DhandhoHome;
import cc.dhandho.ReportMetaInfos;
import cc.dhandho.commons.container.Container;
import cc.dhandho.commons.container.ContainerImpl;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.impl.ReportEngineImpl;
import cc.dhandho.server.CorpInfoDbUpgrader;
import cc.dhandho.server.DbProvider;
import cc.dhandho.server.WashedDataUpgrader;
import cc.dhandho.util.DbInitUtil;
import junit.framework.TestCase;

public class ReportEngineTest extends TestCase {

	public void test() {

		Container app = new ContainerImpl();
		DbProvider dbProvider = TestUtil.newInMemoryTestDbProvider(true);
		dbProvider.executeWithDbSession(new DbInitUtil());

		DbReportMetaInfos metaInfos = new DbReportMetaInfos();
		dbProvider.executeWithDbSession(metaInfos.initializer());

		app.addComponent(DhandhoHome.class, TestUtil.getHome());
		app.addComponent(DbProvider.class, dbProvider);
		app.addComponent(ReportMetaInfos.class, metaInfos);
		//

		CorpInfoDbUpgrader dbu = app.newInstance(CorpInfoDbUpgrader.class);
		dbProvider.executeWithDbSession(dbu);
		// load washed data to DB.
		WashedDataUpgrader wdu = app.newInstance(WashedDataUpgrader.class);
		dbProvider.executeWithDbSession(wdu);

		ReportEngine re = app.newInstance(ReportEngineImpl.class);
		String corpId = "000002";
		int year = 2016;
		String metric = "资产总计";
		Double d = re.getMetricValue(corpId, year, metric);
		System.out.println(d);
	}
}
