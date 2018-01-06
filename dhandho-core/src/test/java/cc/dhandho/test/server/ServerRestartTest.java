package cc.dhandho.test.server;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;

import cc.dhandho.DhoDataHome;
import cc.dhandho.graphdb.DefaultDbProvider;
import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.rest.server.DhoServer;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class ServerRestartTest extends TestCase {

	public static class MyDbProvider extends DefaultDbProvider {
		private static OrientDB ORIENT;

		public MyDbProvider() {

		}

		@Override
		protected OrientDB getOrient() {
			if (ORIENT == null) {
				super.getOrient();
				ORIENT = super.orient;
			}
			return ORIENT;
		}

		@Override
		protected ODatabaseSession openDB() {
			return super.openDB();
		}

	}

	public void testServerRestartOnSameDbAndHome() {
		DhoDataHome home = TestUtil.newEmptyHome();

		DbProvider dbp = new MyDbProvider().dbConfig(TestUtil.newInMemoryTestDbConfig());
		DhoServer server = TestUtil.newInMemoryTestDhandhoServer(dbp);
		server.start();
		server.shutdown();
		
		server.start();
		server.shutdown();
		
	}

}
