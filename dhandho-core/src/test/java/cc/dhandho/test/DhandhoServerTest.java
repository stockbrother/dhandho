package cc.dhandho.test;

import java.io.IOException;

import org.junit.Test;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.DbAliasInfos;
import cc.dhandho.Processor;
import cc.dhandho.server.DhandhoServer;
import junit.framework.Assert;
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

		ds.executeWithDbSession(new Processor<ODatabaseSession>() {

			@Override
			public void process(ODatabaseSession t) {
				aliasInfos.initialize(t);
				String cName = aliasInfos.getColumnNameByAlias("BALSHEET", "资产总计");
				Assert.assertNotNull(cName);
				
			}
		});

		ds.shutdown();
	}
}
