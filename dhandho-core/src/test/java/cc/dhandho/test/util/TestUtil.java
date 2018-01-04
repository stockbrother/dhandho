package cc.dhandho.test.util;

import java.io.IOException;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.container.impl.ContainerImpl;
import com.orientechnologies.orient.core.db.ODatabaseType;

import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.DhoDataHome;
import cc.dhandho.ReportMetaInfos;
import cc.dhandho.client.DhandhoCliConsole;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.graphdb.DefaultDbProvider;
import cc.dhandho.graphdb.MyDataUpgraders;
import cc.dhandho.input.loader.CorpInfoInputDataLoader;
import cc.dhandho.input.loader.WashedInputDataLoader;
import cc.dhandho.report.MetricDefines;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.impl.ReportEngineImpl;
import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.rest.server.DhoServer;
import cc.dhandho.rest.server.DhandhoServerImpl;

public class TestUtil {
	private static final Logger LOG = LoggerFactory.getLogger(TestUtil.class);

	private static DhoDataHome HOME;

	public static DbConfig newInMemoryTestDbConfig() {
		return new DbConfig().dbName("test").dbUrl("memory:test").dbUser("admin").dbPassword("admin")
				.dbType(ODatabaseType.MEMORY);
	}

	public static DbProvider newInMemoryTestDb() {
		DbProvider rt = new DefaultDbProvider().dbConfig(newInMemoryTestDbConfig());
		return rt;
	}

	public static DbConfig newRemoteLocalhostDbConfig() {
		return new DbConfig().dbName("test").dbUrl("remote:localhost").dbUser("admin").dbPassword("admin")
				.dbType(ODatabaseType.PLOCAL);
	}

	public static FileObject newRamFile() {
		try {

			FileObject rt = newRamFolder().resolveFile("ram.file");
			rt.createFile();
			return rt;
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public static FileObject newRamFolder() {
		return newAnyFolder("ram://tmp-folder-");
	}

	public static FileObject newTmpFolder() throws IOException {
		return newAnyFolder("tmp://tmp-folder-");
	}

	private static FileObject newAnyFolder(String prefix) {
		try {

			FileSystemManager fsm = VFS.getManager();
			int i = 0;
			while (true) {
				FileObject rt = fsm.resolveFile(prefix + (i++));
				if (!rt.exists()) {
					rt.createFolder();
					return rt;
				}
			}
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public static DhoDataHome newEmptyRamHome() {
		try {
			FileSystemManager fsm = VFS.getManager();
			FileObject homeF = TestUtil.newAnyFolder("ram://dho-home-");
			return new DhoDataHome(fsm, homeF);
		} catch (FileSystemException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public static DhoDataHome getHome() {

		try {
			if (HOME == null) {

				FileSystemManager fsm = VFS.getManager();
				String from = "res:cc/dhandho/test/dhandho";
				String home = "tmp://dahandho-test-home";
				FileObject fromF = fsm.resolveFile(from);
				FileObject homeF = fsm.resolveFile(home);
				if (homeF.exists()) {
					throw new JcpsException("home already there:" + home);
				}
				LOG.info("test home:" + home + " is ready.");

				homeF.copyFrom(fromF, new AllFileSelector());

				HOME = new DhoDataHome(fsm, home);
			}
			return HOME;
		} catch (FileSystemException e) {
			throw JcpsException.toRtException(e);
		}

	}

	public static MetricDefines newMetricDefines() {
		DhoDataHome home = getHome();
		FileObject file;
		try {
			file = home.resolveFile(home.getClientFile(), "metric-defines.xml");
			return MetricDefines.load(file.getContent().getInputStream());
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public static DbProvider newInMemoryTestDbProvider(boolean create) {
		DbProvider rt = new DefaultDbProvider().dbConfig(newInMemoryTestDbConfig());
		if (create) {
			rt.createDbIfNotExist();
		}
		return rt;

	}

	public static DhoServer newInMemoryTestDhandhoServer() {
		return newInMemoryTestDhandhoServer(TestUtil.newInMemoryTestDb());
	}

	public static DhoServer newInMemoryTestDhandhoServer(DbProvider dbp) {
		return newInMemoryTestDhandhoServer(dbp, getHome());
	}

	public static DhoServer newInMemoryTestDhandhoServer(DhoDataHome home) {
		return newInMemoryTestDhandhoServer(TestUtil.newInMemoryTestDb(), home);
	}

	public static DhoServer newInMemoryTestDhandhoServer(DbProvider dbp, DhoDataHome home) {

		return new DhandhoServerImpl(dbp).dataHome(home);
	}

	public static DhandhoCliConsole newInMemoryTestDhandhoServerConsole() {

		return new DhandhoCliConsole().server(newInMemoryTestDhandhoServer());
	}

	public static ReportEngine newInMemoryReportEgine(DbProvider dbProvider) {
		try {

			DhoDataHome home = getHome();
			Container app = new ContainerImpl();
			ReportMetaInfos metaInfos = new DbReportMetaInfos();
			MetricDefines metricDefines = MetricDefines.load(home.getClientFile().resolveFile("metric-defines.xml"));
			app.addComponent(DhoDataHome.class, home);
			app.addComponent(MetricDefines.class, metricDefines);
			app.addComponent(ReportMetaInfos.class, metaInfos);
			app.addComponent(DbProvider.class, dbProvider);
			ReportEngine reportEngine = app.addComponent(ReportEngine.class, ReportEngineImpl.class);

			dbProvider.createDbIfNotExist();

			dbProvider.executeWithDbSession(new MyDataUpgraders());

			// load corp info to DB.
			CorpInfoInputDataLoader dbu = app.newInstance(CorpInfoInputDataLoader.class);
			dbProvider.executeWithDbSession(dbu);
			// load washed data to DB.
			WashedInputDataLoader wdu = app.newInstance(WashedInputDataLoader.class);
			dbProvider.executeWithDbSession(wdu);

			return reportEngine;
		} catch (IOException e) {
			throw new JcpsException(e);
		}
	}
}
