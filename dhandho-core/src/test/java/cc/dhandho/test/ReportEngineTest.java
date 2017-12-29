package cc.dhandho.test;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.AppContext;
import cc.dhandho.AppContextImpl;
import cc.dhandho.DbAliasInfos;
import cc.dhandho.DhandhoHome;
import cc.dhandho.Processor;
import cc.dhandho.ReportMetaInfos;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.impl.ReportEngineImpl;
import cc.dhandho.server.CorpInfoDbUpgrader;
import cc.dhandho.server.DbProvider;
import cc.dhandho.server.WashedDataUpgrader;
import cc.dhandho.util.DbInitUtil;
import junit.framework.TestCase;

public class ReportEngineTest extends TestCase {

	public void test() {

		AppContext app = new AppContextImpl();
		DbProvider dbProvider = TestUtil.newInMemoryTestDbProvider(true);
		dbProvider.executeWithDbSession(new DbInitUtil());
		app.addComponent(DhandhoHome.class, TestUtil.getHome());
		app.addComponent(DbProvider.class, dbProvider);

		CorpInfoDbUpgrader dbu = app.newInstance(CorpInfoDbUpgrader.class);
		dbProvider.executeWithDbSession(dbu);
		// load washed data to DB.
		WashedDataUpgrader wdu = app.newInstance(WashedDataUpgrader.class);
		dbProvider.executeWithDbSession(wdu);

		DbAliasInfos rmi = new DbAliasInfos();
		app.addComponent(ReportMetaInfos.class, rmi);
		dbProvider.executeWithDbSession(new Processor<ODatabaseSession>() {

			@Override
			public void process(ODatabaseSession t) {
				rmi.initialize(t);

			}
		});

		ReportEngine re = app.newInstance(ReportEngineImpl.class);
		String corpId = "000002";
		int year = 2016;
		String metric = "资产总计";
		Double d = re.getMetricValue(corpId, year, metric);
		System.out.println(d);
	}
}
