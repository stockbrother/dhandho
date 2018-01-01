package cc.dhandho.test.util;

import java.io.IOException;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.db.ODatabaseType;

import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.DhandhoHome;
import cc.dhandho.ReportMetaInfos;
import cc.dhandho.RtException;
import cc.dhandho.client.DhandhoCliConsole;
import cc.dhandho.commons.container.Container;
import cc.dhandho.commons.container.ContainerImpl;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.graphdb.DefaultDbProvider;
import cc.dhandho.report.MetricDefines;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.impl.ReportEngineImpl;
import cc.dhandho.rest.server.CorpInfoDbUpgrader;
import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.rest.server.DhandhoServer;
import cc.dhandho.rest.server.DhandhoServerImpl;
import cc.dhandho.rest.server.WashedDataUpgrader;
import cc.dhandho.util.DbInitUtil;

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

	public static FileObject newRamFile() throws IOException {
		FileObject rt = newRamFolder().resolveFile("ram.file");
		rt.createFile();
		return rt;
	}

	public static FileObject newRamFolder() throws IOException {
		return newAnyFolder("ram://tmp-folder-");
	}

	public static FileObject newTmpFolder() throws IOException {
		return newAnyFolder("tmp://tmp-folder-");
	}

	private static FileObject newAnyFolder(String prefix) throws IOException {
		FileSystemManager fsm = VFS.getManager();
		int i = 0;
		while (true) {
			FileObject rt = fsm.resolveFile(prefix + (i++));
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

	public static ReportEngine newInMemoryReportEgine() {
		try {
			DbProvider dbProvider = TestUtil.newInMemoryTestDbProvider(true);
			DhandhoHome home = getHome();
			Container app = new ContainerImpl();
			ReportMetaInfos metaInfos = new DbReportMetaInfos();
			MetricDefines metricDefines = MetricDefines.load(home.getClientFile().resolveFile("metric-defines.xml"));
			app.addComponent(DhandhoHome.class, home);
			app.addComponent(MetricDefines.class, metricDefines);
			app.addComponent(ReportMetaInfos.class, metaInfos);
			app.addComponent(DbProvider.class, dbProvider);
			ReportEngine reportEngine = app.addNewComponent(ReportEngine.class, ReportEngineImpl.class);
			
			dbProvider.createDbIfNotExist();

			dbProvider.executeWithDbSession(new DbInitUtil());

			// load corp info to DB.
			CorpInfoDbUpgrader dbu = app.newInstance(CorpInfoDbUpgrader.class);
			dbProvider.executeWithDbSession(dbu);
			// load washed data to DB.
			WashedDataUpgrader wdu = app.newInstance(WashedDataUpgrader.class);
			dbProvider.executeWithDbSession(wdu);

			return reportEngine;
		} catch (IOException e) {
			throw new RtException(e);
		}
	}
}
