package cc.dhandho.test;

import com.orientechnologies.orient.core.db.ODatabaseType;

import cc.dhandho.DhandhoHome;
import cc.dhandho.client.DhandhoCliConsole;
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

	public static DhandhoHome getHome() {
		return new DhandhoHome("res:cc/dhandho/test/dhandho");
	}
	
	public static DhandhoServer newInMemoryTestDhandhoServer() {
		
		return new DhandhoServerImpl().home(getHome()).dbConfig(newInMemoryTestDbConfig());
	}

	public static DhandhoCliConsole newInMemoryTestDhandhoServerConsole() {

		return new DhandhoCliConsole().server(newInMemoryTestDhandhoServer());
	}
}
