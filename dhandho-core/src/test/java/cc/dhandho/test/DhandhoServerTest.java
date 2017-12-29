package cc.dhandho.test;

import java.io.IOException;

import org.junit.Test;

import cc.dhandho.DbAliasInfos;
import cc.dhandho.server.DhandhoServer;
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

		DbAliasInfos aliasInfos = new DbAliasInfos();

		ds.shutdown();
	}
}
