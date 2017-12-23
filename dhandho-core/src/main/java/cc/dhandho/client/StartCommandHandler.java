package cc.dhandho.client;

import com.orientechnologies.orient.core.db.ODatabaseType;

import cc.dhandho.RtException;
import cc.dhandho.commons.commandline.CommandAndLine;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.graphdb.Handler;
import cc.dhandho.server.DhandhoServer;
import cc.dhandho.server.impl.DhandhoServerImpl;

public class StartCommandHandler implements CommandHandler {

	@Override
	public void execute(CommandAndLine line) {
		DhandhoConsole console = (DhandhoConsole) line.getConsole();
		DhandhoServer server = console.getServer();
		if (server == null) {

			server = new DhandhoServerImpl().dbConfig(newInMemoryTestDbConfig());
			server.start();

			console.setServer(server);
			DhandhoServer serverF = server;
			console.addBeforeShutdownHandler(new Handler() {

				@Override
				public void execute() {
					serverF.shutdown();
					console.setServer(null);
				}
			});

		} else {
			line.getConsole().peekWriter().writeLine("server is already exists.");			
		}

	}

	public static DbConfig newInMemoryTestDbConfig() {
		return new DbConfig().dbName("test").dbUrl("memory:test").dbUser("admin").dbPassword("admin")
				.dbType(ODatabaseType.MEMORY);
	}

}
