package cc.dhandho.server.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.DhandhoHome;
import cc.dhandho.Processor;
import cc.dhandho.commons.container.Container;
import cc.dhandho.commons.container.ContainerImpl;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.graphdb.DefaultDbProvider;
import cc.dhandho.rest.DbSessionTL;
import cc.dhandho.rest.JsonHandlers;
import cc.dhandho.server.CorpInfoDbUpgrader;
import cc.dhandho.server.DbProvider;
import cc.dhandho.server.DhandhoServer;
import cc.dhandho.server.WashedDataUpgrader;
import cc.dhandho.util.DbInitUtil;

/**
 * 
 * @author Wu
 *
 */
public class DhandhoServerImpl implements DhandhoServer, Thread.UncaughtExceptionHandler, ThreadFactory {
	// private static final Logger LOG = LoggerFactory.getLogger();
	private static final Logger LOG = LoggerFactory.getLogger(DhandhoServerImpl.class);

	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	JsonHandlers handlers;

	private OrientDB orient;

	Container app;

	protected DhandhoHome home;

	protected DefaultDbProvider dbProvider;

	private ScheduledExecutorService executor;

	public DhandhoServerImpl() {
		this.dbProvider = new DefaultDbProvider();
	}

	@Override
	public void start() {
		if (LOG.isInfoEnabled()) {
			LOG.info("start...");
		}

		this.executor = Executors.newScheduledThreadPool(1, this);
		app = new ContainerImpl();
		app.addComponent(DbProvider.class, this.dbProvider);
		app.addComponent(DhandhoHome.class, home);
		app.addComponent(AllQuotesInfos.class, new AllQuotesInfos());

		handlers = new JsonHandlers(app);

		this.dbProvider.createDbIfNotExist();

		this.dbProvider.executeWithDbSession(new DbInitUtil());

		// load corp info to DB.
		CorpInfoDbUpgrader dbu = app.newInstance(CorpInfoDbUpgrader.class);
		this.dbProvider.executeWithDbSession(dbu);
		// load washed data to DB.
		WashedDataUpgrader wdu = app.newInstance(WashedDataUpgrader.class);
		this.dbProvider.executeWithDbSession(wdu);

		if (LOG.isInfoEnabled()) {
			LOG.info("start done");
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
		this.orient.close();
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
	public DhandhoServer home(DhandhoHome home) {
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
	public DhandhoHome getHome() {
		return home;
	}

	@Override
	public DhandhoServer dbConfig(DbConfig config) {
		this.dbProvider.dbConfig(config);
		return this;
	}

}
