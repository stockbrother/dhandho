package cc.dhandho.test.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.junit.Assert;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.container.impl.ContainerImpl;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
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
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.rest.RestRequestHandler;
import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.rest.server.DhandhoServerImpl;
import cc.dhandho.rest.server.DhoServer;
import cc.dhandho.rest.server.MetricDefinesLoader;
import cc.dhandho.rest.web.JettyWebServer;
import cc.dhandho.util.JsonUtil;

public class TestUtil {
	private static final Logger LOG = LoggerFactory.getLogger(TestUtil.class);

	private static DhoDataHome HOME;

	public static DbConfig newInMemoryTestDbConfig() {
		return new DbConfig().dbName("test").dbUrl("memory:test").dbUser("admin").dbPassword("admin")
				.dbType(ODatabaseType.MEMORY);
	}

	public static DbProvider newInMemoryTestDbProvider() {
		return TestUtil.newInMemoryTestDbProvider(false);
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

	public static FileObject newAnyFolder(String prefix) {
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

	public static DhoDataHome getTheDefaultPopulatedDataHomeForTest() {
		try {
			if (HOME == null) {

				FileSystemManager fsm = VFS.getManager();
				String from = "res:cc/dhandho/test/dhandho";
				String home = "tmp://dahandho-test-home-default";
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

	public static DhoDataHome newEmptyHome() {

		try {
			if (HOME == null) {

				FileSystemManager fsm = VFS.getManager();

				String home = "tmp://dahandho-test-home-";
				FileObject fo = TestUtil.newAnyFolder(home);
				HOME = new DhoDataHome(fsm, fo);
			}
			return HOME;
		} catch (FileSystemException e) {
			throw JcpsException.toRtException(e);
		}

	}

	public static MetricDefines newMetricDefines() {
		DhoDataHome home = newEmptyHome();
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
		return newInMemoryTestDhandhoServer(TestUtil.newInMemoryTestDbProvider());
	}

	public static DhoServer newInMemoryTestDhandhoServer(DbProvider dbp) {
		return newInMemoryTestDhandhoServer(dbp, newEmptyHome());
	}

	public static DhoServer newInMemoryTestDhandhoServer(DhoDataHome home) {
		return newInMemoryTestDhandhoServer(TestUtil.newInMemoryTestDbProvider(), home);
	}

	public static DhoServer newInMemoryTestDhandhoServer(DbProvider dbp, DhoDataHome home) {

		return new DhandhoServerImpl(dbp).dataHome(home);
	}

	public static DhandhoCliConsole newInMemoryTestDhandhoServerConsole() {

		return new DhandhoCliConsole(newConsoleHome()).server(newInMemoryTestDhandhoServer());
	}
	
	public static Container populateDbWithDefaultDataHomeForTest(Container app) {
		DbProvider dbProvider = TestUtil.newInMemoryTestDbProvider();
		DhoDataHome dataHome = TestUtil.getTheDefaultPopulatedDataHomeForTest();
		ReportMetaInfos metaInfos = new DbReportMetaInfos();
		MetricDefines metricDefines = new MetricDefinesLoader().load(dataHome);
		app.addComponent(DhoDataHome.class, dataHome);
		app.addComponent(MetricDefines.class, metricDefines);	
		app.addComponent(ReportMetaInfos.class, metaInfos);
		app.addComponent(DbProvider.class, dbProvider);

		dbProvider.createDbIfNotExist();
		dbProvider.executeWithDbSession(new MyDataUpgraders());

		// load corp info to DB.
		CorpInfoInputDataLoader dbu = app.newInstance(CorpInfoInputDataLoader.class);
		dbProvider.executeWithDbSession(dbu);
		// load washed data to DB.
		WashedInputDataLoader wdu = app.newInstance(WashedInputDataLoader.class);
		dbProvider.executeWithDbSession(wdu);

		return app;
	}

	public static ReportEngine newInMemoryReportEgine(DbProvider dbProvider) {

		DhoDataHome dataHome = TestUtil.getTheDefaultPopulatedDataHomeForTest();
		Container app = new ContainerImpl();
		ReportMetaInfos metaInfos = new DbReportMetaInfos();
		MetricDefines metricDefines = new MetricDefinesLoader().load(dataHome);
		app.addComponent(DhoDataHome.class, dataHome);
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

	}

	public static FileObject newConsoleHome() {
		//
		return TestUtil.newRamFolder();
	}

	public static Container newContainerWithDefaultTestHome() {
		DhoDataHome dataHome = TestUtil.getTheDefaultPopulatedDataHomeForTest();
		Container app = new ContainerImpl();
		app.addComponent(DhoDataHome.class, dataHome);
		return app;
	}

	public static DhoServer mockDhoServerWithHandler(RestRequestHandler handler) {
		DhoServer dserver = mock(DhoServer.class);
		String handlerS = handler.getClass().getName();
		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {

				Object[] arguments = invocation.getArguments();
				if (arguments.length == 3) {
					String handlerName = (String) arguments[0];
					Reader r = (Reader) arguments[1];
					Writer w = (Writer) arguments[2];

					JsonReader jR = new JsonReader(r);
					JsonWriter jW = new JsonWriter(w);
					RestRequestContext rrc = new RestRequestContext(jR, jW);
					handler.handle(rrc);

				} else {
					throw new Exception("not supported.");
				}
				return null;
			}
		}).when(dserver).handle(any(String.class), any(Reader.class), any(Writer.class));

		doAnswer(new Answer<JsonElement>() {

			@Override
			public JsonElement answer(InvocationOnMock invocation) throws Throwable {

				Object[] arguments = invocation.getArguments();
				if (arguments.length == 2) {
					String handlerName = (String) arguments[0];
					JsonElement r = (JsonElement) arguments[1];
					JsonReader jR = JsonUtil.toJsonReader(r);
					StringWriter sWriter = new StringWriter();
					JsonWriter jW = new JsonWriter(sWriter);
					RestRequestContext rrc = new RestRequestContext(jR, jW);
					handler.handle(rrc);
					return JsonUtil.parse(sWriter.getBuffer().toString());
				} else {
					throw new Exception("not supported.");
				}
			}
		}).when(dserver).handle(any(String.class), any(JsonElement.class));

		return dserver;
	}

	public static void runWithWebServer(JettyWebServer web) {
		web.start();
		try {

		} finally {
			web.shutdown();
		}
	}

	public static void assertEqual(JsonElement je1, JsonElement je2) {
		Assert.assertTrue(je1 == null && je2 == null || je1 != null && je2 != null);
		String jr1 = JsonUtil.toString(je1);
		String jr2 = JsonUtil.toString(je2);
		Assert.assertEquals(jr1, jr2);
	}

}
