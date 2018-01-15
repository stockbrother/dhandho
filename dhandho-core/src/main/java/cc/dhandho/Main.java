package cc.dhandho;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.JcpsException;
import com.orientechnologies.orient.core.db.ODatabaseType;

import cc.dhandho.client.jfx.DhandhoJfxApplication;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.graphdb.DefaultDbProvider;
import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.rest.server.DhandhoServerImpl;
import cc.dhandho.rest.server.DhoServer;
import cc.dhandho.rest.web.JettyWebServer;

public class Main {
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);
	private static final boolean bootConsole = false;
	private static final boolean bootWebServer = true;

	DhoDataHome home;

	DbProvider dbProvider;

	DhoServer server;

	JettyWebServer web;

	public static void main(String[] args) {
		new Main().boot();
	}

	private void boot() {

		home = getDataHome();
		dbProvider = getDbProvider(home);
		server = new DhandhoServerImpl(dbProvider).dataHome(home);
		server.start();

		if (bootWebServer) {
			web = new JettyWebServer(server);
			web.start();
		}
/*
		if (bootConsole) {
			FileObject consoleHome = getConsoleHome();
			DhandhoJfxApplication.launch(server, consoleHome);
		}
*/
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				if (web != null) {
					web.shutdown();
				}
				
				server.shutdown();
				dbProvider.close();
			}

		});

	}

	private static DbProvider getDbProvider(DhoDataHome home) {
		return new DefaultDbProvider().dbConfig(getDbConfig(home));
	}

	private static DbConfig getDbConfig(DhoDataHome home) {
		String dbUrl = getDbUrl(home);
		return new DbConfig()//
				.dbUrl(dbUrl)//
				.dbName("test")//
				.dbUser("admin")//
				.dbPassword("admin")//
				.dbType(ODatabaseType.PLOCAL)//
		;
	}

	private static String getDbUrl(DhoDataHome home) {
		String url = System.getProperty("dhandho.db.url");
		if (url == null) {
			String userHome = System.getProperty("user.home");
			userHome = userHome.replace('\\', '/');
			url = "plocal://" + userHome + "/.dhandho/orientdb";
		}
		return url;
	}

	private static FileObject getConsoleHome() {
		String home = System.getProperty("dhandho.console.home");

		if (home == null) {
			String userHome = System.getProperty("user.home");
			userHome = userHome.replace('\\', '/');
			home = "file://" + userHome + "/.dhandho/console";
		}
		try {
			FileSystemManager fsm = VFS.getManager();
			FileObject fo = fsm.resolveFile(home);
			if (!fo.exists()) {
				fo.createFolder();
				LOG.info("console home is ready.");
			}
			return fo;
		} catch (FileSystemException e) {
			throw JcpsException.toRtException(e);
		}
	}

	private static DhoDataHome getDataHome() {
		String home = System.getProperty("dhandho.data.home");

		if (home == null) {
			String userHome = System.getProperty("user.home");
			userHome = userHome.replace('\\', '/');
			home = "file://" + userHome + "/.dhandho/data";
		}
		try {
			FileSystemManager fsm = VFS.getManager();
			FileObject fo = fsm.resolveFile(home);
			if (!fo.exists()) {
				fo.createFolder();
				LOG.info("data home is ready.");
			}

			DhoDataHome rt = new DhoDataHome(fsm, home);
			return rt;
		} catch (FileSystemException e) {
			throw JcpsException.toRtException(e);
		}
	}

}
