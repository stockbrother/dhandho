package cc.dhandho.rest.server;

import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	protected DhoDataHome dataHome;

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

		new HomeFolderInitializer().initHomeFolder(dataHome);

		ReportMetaInfos metaInfos = new DbReportMetaInfos();
		MetricDefines metricDefines = new MetricDefinesLoader().load(dataHome);
		this.executor = Executors.newScheduledThreadPool(1, this);
		app = new ContainerImpl();
		app.addComponent(DhoDataHome.class, dataHome);
		app.addComponent(MetricDefines.class, metricDefines);
		app.addComponent(ReportMetaInfos.class, metaInfos);
		app.addComponent(DbProvider.class, this.dbProvider);

		ReportEngine reportEngine = app.newInstance(ReportEngineImpl.class);
		app.addComponent(ReportEngine.class, reportEngine);
		AllQuotesInfos allQuotesInfos = new AllQuotesInfos();
		app.addComponent(AllQuotesInfos.class, allQuotesInfos);
		InputDataMainLoader loader = app.addComponent(InputDataMainLoader.class, InputDataMainLoader.class);
		this.dbProvider.createDbIfNotExist();
		this.dbProvider.executeWithDbSession(new MyDataUpgraders());
		handlers = new JsonHandlers(app);

		// load it at server start.
		loader.execute();
		this.dbProvider.executeWithDbSession(((DbReportMetaInfos) metaInfos).initializer());
		if (LOG.isInfoEnabled()) {
			LOG.info("done of start.");
		}

	}

	/**
	 * If the home folder is not init before. And it is empty, then init it with the
	 * default content from res:// folder.
	 */

	@Override
	public void shutdown() {
		this.executor.shutdown();
		try {
			this.executor.awaitTermination(100, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			throw new RuntimeException("", e);
		}
		this.dbProvider.close();
	}

	@Override
	public void handle(final String handlerS, Reader reader, final Writer writer) {
		handlers.handle(handlerS, reader, writer);
	}

	@Override
	public void handle(final String handlerS) {
		handlers.handle(handlerS);
	}

	public void handle(final String handlerS, JsonReader reader, final JsonWriter writer) {
		handlers.handle(handlerS, reader, writer);
	}

	@Override
	public JsonElement handle(String handlerS, JsonElement request) {
		return handlers.handle(handlerS, request);
	}

	@Override
	public DhoServer dataHome(DhoDataHome home) {
		this.dataHome = home;
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
	public DhoDataHome getDataHome() {
		return dataHome;
	}

}
