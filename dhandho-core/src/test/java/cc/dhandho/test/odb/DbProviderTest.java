package cc.dhandho.test.odb;

import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class DbProviderTest extends TestCase {

	public void test() {
		DbProvider dbp = TestUtil.newInMemoryTestDb();
		dbp.createDbIfNotExist();
		System.out.println("done.");

	}
}
