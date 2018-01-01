package cc.dhandho.test;

import java.io.IOException;

import org.junit.Test;

import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.rest.server.DhandhoServer;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class DhandhoServerTest extends TestCase {

	@Override
	public void setUp() {

	}

	@Override
	public void tearDown() {

	}

	@Test
	public void testNoTx() throws IOException {
		DhandhoServer ds = TestUtil.newInMemoryTestDhandhoServer();
		ds.start();

		DbReportMetaInfos aliasInfos = new DbReportMetaInfos();

		ds.shutdown();
	}
}
