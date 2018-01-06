package cc.dhandho.test.sina;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.DhoDataHome;
import cc.dhandho.rest.server.AllQuotesLoader;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class MemoryAllQuotesWashedDataLoaderTest extends TestCase {

	public void test() {
		DhoDataHome home = TestUtil.newEmptyHome();

		AllQuotesInfos allQuotesInfos = new AllQuotesInfos();
		AllQuotesLoader l = new AllQuotesLoader();
		long time = System.currentTimeMillis();
		l.load(home, allQuotesInfos);

		TestCase.assertNotNull(allQuotesInfos.getBuyPrice("000001"));
		TestCase.assertNotNull(allQuotesInfos.getBuyPrice("000002"));
		TestCase.assertNotNull(allQuotesInfos.getBuyPrice("000005"));
		TestCase.assertNotNull(allQuotesInfos.getBuyPrice("000100"));

		TestCase.assertTrue(allQuotesInfos.getLastLoaded() >= time);

	}
}
