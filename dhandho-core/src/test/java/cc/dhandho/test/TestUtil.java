package cc.dhandho.test;

import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.server.DhandhoServer;
import cc.dhandho.server.impl.DhandhoServerImpl;

public class TestUtil {

	public static DbConfig newInMemoryTestDbConfig() {
		return new DbConfig().dbName("test").dbUrl("memory:test").dbUser("admin").dbPassword("admin");
	}
	public static DbConfig newRemoteLocalhostDbConfig() {
		return new DbConfig().dbName("test").dbUrl("remote:localhost").dbUser("admin").dbPassword("admin");
	}

	public static DhandhoServer newInMemoryTestDhandhoServer() {
		return new DhandhoServerImpl().dbConfig(newInMemoryTestDbConfig());
	}
}
