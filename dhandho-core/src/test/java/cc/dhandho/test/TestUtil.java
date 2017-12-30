package cc.dhandho.test;

import java.io.IOException;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.db.ODatabaseType;

import cc.dhandho.DhandhoHome;
import cc.dhandho.RtException;
import cc.dhandho.client.DhandhoCliConsole;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.graphdb.DefaultDbProvider;
import cc.dhandho.report.MetricDefines;
import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.rest.server.DhandhoServer;
import cc.dhandho.rest.server.DhandhoServerImpl;

public class TestUtil {
	private static final Logger LOG = LoggerFactory.getLogger(TestUtil.class);

	private static DhandhoHome HOME;

	public static DbConfig newInMemoryTestDbConfig() {
		return new DbConfig().dbName("test").dbUrl("memory:test").dbUser("admin").dbPassword("admin")
				.dbType(ODatabaseType.MEMORY);
	}

	public static DbConfig newRemoteLocalhostDbConfig() {
		return new DbConfig().dbName("test").dbUrl("remote:localhost").dbUser("admin").dbPassword("admin")
				.dbType(ODatabaseType.PLOCAL);
	}

	public static FileObject newTempFolder() throws IOException {
		FileSystemManager fsm = VFS.getManager();
		int i = 0;
		while (true) {
			FileObject rt = fsm.resolveFile("tmp://tmp-folder-" + (i++));
			if (!rt.exists()) {
				rt.createFolder();
				return rt;
			}
		}
	}

	public static DhandhoHome getHome() {

		try {
			if (HOME == null) {

				FileSystemManager fsm = VFS.getManager();
				String from = "res:cc/dhandho/test/dhandho";
				String home = "tmp://dahandho-test-home";
				FileObject fromF = fsm.resolveFile(from);
				FileObject homeF = fsm.resolveFile(home);
				if (homeF.exists()) {
					throw new RtException("home already there:" + home);
				}
				LOG.info("test home:" + home + " is ready.");

				homeF.copyFrom(fromF, new AllFileSelector());

				HOME = new DhandhoHome(fsm, home);
			}
			return HOME;
		} catch (FileSystemException e) {
			throw RtException.toRtException(e);
		}

	}

	public static MetricDefines newMetricDefines() {
		DhandhoHome home = getHome();
		FileObject file;
		try {
			file = home.resolveFile(home.getClientFile(), "metric-defines.xml");
			return MetricDefines.load(file.getContent().getInputStream());
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}
	}

	public static DbProvider newInMemoryTestDbProvider(boolean create) {
		DbProvider rt = new DefaultDbProvider().dbConfig(newInMemoryTestDbConfig());
		if (create) {
			rt.createDbIfNotExist();
		}
		return rt;

	}

	public static DhandhoServer newInMemoryTestDhandhoServer() {

		return new DhandhoServerImpl().home(getHome()).dbConfig(newInMemoryTestDbConfig());
	}

	public static DhandhoCliConsole newInMemoryTestDhandhoServerConsole() {

		return new DhandhoCliConsole().server(newInMemoryTestDhandhoServer());
	}
}
