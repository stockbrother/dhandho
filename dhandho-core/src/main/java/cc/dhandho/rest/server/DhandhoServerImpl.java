package cc.dhandho.rest.server;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.container.impl.ContainerImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.DhoDataHome;
import cc.dhandho.ReportMetaInfos;
import cc.dhandho.graphdb.MyDataUpgraders;
import cc.dhandho.input.loader.ArchivableCorpInfoInputDataLoader;
import cc.dhandho.input.loader.ArchivableWashedInputDataLoader;
import cc.dhandho.input.washed.MemoryAllQuotesWashedDataLoader;
import cc.dhandho.report.MetricDefines;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.impl.ReportEngineImpl;
import cc.dhandho.rest.JsonHandlers;

/**
 * 
 * @author Wu
 *
 */
public class DhandhoServerImpl implements DhoServer, Thread.UncaughtExceptionHandler, ThreadFactory {
	// private static final Logger LOG = LoggerFactory.getLogger();
	private static final Logger LOG = LoggerFactory.getLogger(DhandhoServerImpl.class);

	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	JsonHandlers handlers;

	Container app;

	protected DhoDataHome home;

	protected DbProvider dbProvider;

	private ScheduledExecutorService executor;

	public DhandhoServerImpl(DbProvider dbProvider) {
		this.dbProvider = dbProvider;
	}

	@Override
	public void start() {
		if (LOG.isInfoEnabled()) {
			LOG.info("start...");
		}

		initHomeFolder();

		ReportMetaInfos metaInfos = new DbReportMetaInfos();
		MetricDefines metricDefines = new MetricDefinesLoader().load(home);
		this.executor = Executors.newScheduledThreadPool(1, this);
		app = new ContainerImpl();
		app.addComponent(DhoDataHome.class, home);
		app.addComponent(MetricDefines.class, metricDefines);
		app.addComponent(ReportMetaInfos.class, metaInfos);
		app.addComponent(DbProvider.class, this.dbProvider);

		ReportEngine reportEngine = app.newInstance(ReportEngineImpl.class);
		app.addComponent(ReportEngine.class, reportEngine);
		AllQuotesInfos allQuotesInfos = new AllQuotesInfos();
		app.addComponent(AllQuotesInfos.class, allQuotesInfos);

		try {

			FileObject to = home.getInputFolder().resolveFile("sina");
			if (to.exists()) {
				LOG.warn("loading all-quotes from folder:" + to.getURL());
				MemoryAllQuotesWashedDataLoader loader = new MemoryAllQuotesWashedDataLoader(to, allQuotesInfos);
				loader.start();
			} else {
				LOG.warn("skip loadin all-quotes since folder not exist:" + to.getURL());
			}
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}

		this.dbProvider.createDbIfNotExist();
		this.dbProvider.executeWithDbSession(new MyDataUpgraders());

		// load corp info to DB.
		ArchivableCorpInfoInputDataLoader dbu = app.newInstance(ArchivableCorpInfoInputDataLoader.class);
		this.dbProvider.executeWithDbSession(dbu);
		// load washed data to DB.
		ArchivableWashedInputDataLoader wdu = app.newInstance(ArchivableWashedInputDataLoader.class);
		this.dbProvider.executeWithDbSession(wdu);

		handlers = new JsonHandlers(app);

		if (LOG.isInfoEnabled()) {
			LOG.info("done of start.");
		}

	}

	/**
	 * If the home folder is not init before. And it is empty, then init it with the
	 * default content from res:// folder.
	 */
	private void initHomeFolder() {
		FileObject homeF = this.home.getHomeFile();
		try {
			FileObject initF = homeF.resolveFile(".dho.init");
			if (initF.exists()) {
				return;
			}

			FileObject[] children = homeF.getChildren();
			if (children.length > 0) {
				throw new JcpsException(
						"home folder is not empty, since it is not inited, it must be empty:" + homeF.getURL());
			}

			FileObject fromF = this.home.getFileSystem().resolveFile("res:cc/dhandho/home");
			LOG.info("test home:" + home + " is ready.");

			homeF.copyFrom(fromF, new AllFileSelector());
			// check archive folder
			FileObject archiveF = home.getArchiveRootFolder();
			if (!archiveF.exists()) {
				archiveF.createFolder();
			}
			// check washed folder.
			FileObject washedF = home.getInportWashedDataFolder();
			if (!washedF.exists()) {
				washedF.createFolder();
			}
			initF.createFile();

		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}

	}

	@Override
	public void shutdown() {
		this.executor.shutdown();
		try {
			this.executor.awaitTermination(100, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			throw new RuntimeException("", e);
		}
	}

	@Override
	public void handle(final String handlerS, Reader reader, final Writer writer) throws IOException {
		handlers.handle(handlerS, reader, writer);
	}

	@Override
	public void handle(final String handlerS) throws IOException {
		handlers.handle(handlerS);
	}

	public void handle(final String handlerS, JsonReader reader, final JsonWriter writer) throws IOException {
		handlers.handle(handlerS, reader, writer);
	}

	@Override
	public JsonElement handle(String handlerS, JsonElement request) throws IOException {
		return handlers.handle(handlerS, request);
	}

	@Override
	public DhoServer home(DhoDataHome home) {
		this.home = home;
		return this;
	}

	public static class AppThread extends Thread {
		DhandhoServerImpl ddr;

		public AppThread(Runnable r, DhandhoServerImpl ddr) {
			super(r);
			this.ddr = ddr;
			this.setUncaughtExceptionHandler(ddr);
		}

		@Override
		public void run() {
			// AppContextImpl.set(this.ddr);
			super.run();
		}
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		LOG.error("uncaughtException in thread:" + thread.getName(), throwable);
	}

	@Override
	public Thread newThread(Runnable r) {
		return new AppThread(r, this);
	}

	@Override
	public DhoDataHome getHome() {
		return home;
	}

}
