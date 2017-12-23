package cc.dhandho.test;

import com.orientechnologies.orient.core.db.ODatabaseType;

import cc.dhandho.client.DhandhoConsole;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.server.DhandhoServer;
import cc.dhandho.server.impl.DhandhoServerImpl;

public class TestUtil {

	public static DbConfig newInMemoryTestDbConfig() {
		return new DbConfig().dbName("test").dbUrl("memory:test").dbUser("admin").dbPassword("admin")
				.dbType(ODatabaseType.MEMORY);
	}

	public static DbConfig newRemoteLocalhostDbConfig() {
		return new DbConfig().dbName("test").dbUrl("remote:localhost").dbUser("admin").dbPassword("admin")
				.dbType(ODatabaseType.PLOCAL);
	}

	public static DhandhoServer newInMemoryTestDhandhoServer() {
		return new DhandhoServerImpl().home("res:cc/dhandho/test/dhandho").dbConfig(newInMemoryTestDbConfig());
	}

	public static DhandhoConsole newInMemoryTestDhandhoServerConsole() {

		return new DhandhoConsole().server(newInMemoryTestDhandhoServer());
	}
}
