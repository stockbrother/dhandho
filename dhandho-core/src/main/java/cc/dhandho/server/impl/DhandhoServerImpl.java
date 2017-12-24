package cc.dhandho.server.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;

import cc.dhandho.AppContext;
import cc.dhandho.AppContextImpl;
import cc.dhandho.DhandhoHome;
import cc.dhandho.Processor;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.rest.DbSessionTL;
import cc.dhandho.rest.JsonHandlers;
import cc.dhandho.server.CorpInfoDbUpgrader;
import cc.dhandho.server.DbProvider;
import cc.dhandho.server.DhandhoServer;
import cc.dhandho.server.WashedDataUpgrader;
import cc.dhandho.util.DbInitUtil;

/**
 * 
 * @author wuzhen
 *
 */
public class DhandhoServerImpl implements DhandhoServer, Thread.UncaughtExceptionHandler, ThreadFactory {
	// private static final Logger LOG = LoggerFactory.getLogger();
	private static final Logger LOG = LoggerFactory.getLogger(DhandhoServerImpl.class);

	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	JsonHandlers handlers;

	private OrientDB orient;

	AppContext app;
	DbConfig dbConfig;

	protected DhandhoHome home;

	private ScheduledExecutorService executor;

	public DhandhoServerImpl dbConfig(DbConfig dbConfig) {
		this.dbConfig = dbConfig;
		return this;
	}

	@Override
	public void start() {
		if (LOG.isInfoEnabled()) {
			LOG.info("start...");
		}

		this.executor = Executors.newScheduledThreadPool(1, this);
		app = new AppContextImpl();
		app.addComponent(DbProvider.class, this);

		app.addComponent(DhandhoHome.class, home);

		orient = new OrientDB(this.dbConfig.getDbUrl(), OrientDBConfig.defaultConfig());
		handlers = new JsonHandlers(app);

		this.createDbIfNotExist();

		this.executeWithDbSession(new DbInitUtil());

		// load corp info to DB.
		CorpInfoDbUpgrader dbu = app.newInstance(CorpInfoDbUpgrader.class);
		this.executeWithDbSession(dbu);
		// load washed data to DB.
		WashedDataUpgrader wdu = app.newInstance(WashedDataUpgrader.class);
		this.executeWithDbSession(wdu);

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
	public boolean createDbIfNotExist() {
		return this.orient.createIfNotExists(this.dbConfig.getDbName(), this.dbConfig.getDbType());
	}

	private ODatabaseSession openDB() {
		// TODO avoid openDB twice.
		// TODO if dbName = null, it will block here. why?
		if (this.dbConfig.getDbName() == null) {
			throw new IllegalArgumentException("no db name set.");
		}

		ODatabaseSession databaseSession = this.orient.open(this.dbConfig.getDbName(), this.dbConfig.getUser(),
				this.dbConfig.getPassword());

		return databaseSession;
	}

	public void executeWithDbSession(Processor<ODatabaseSession> processor) {

		ODatabaseSession dbs = DbSessionTL.get();
		boolean isNew = (dbs == null);
		if (isNew) {
			dbs = this.openDB();
			DbSessionTL.set(dbs);
		}

		try {
			processor.process(dbs);
		} finally {
			if (isNew) {
				DbSessionTL.set(null);//
				dbs.close();
			}
		}

	}

	// TODO add data version for corpInfo and do not load twice for the same version
	// of data.

	private Reader traceReader(Reader reader) throws IOException {
		StringWriter swriter = new StringWriter();
		char[] cbuf = new char[1024];
		while (true) {
			int len = reader.read(cbuf);
			if (len == -1) {
				break;
			}
			swriter.write(cbuf, 0, len);

		}
		LOG.trace(":start of request.reader:");
		LOG.trace(swriter.getBuffer().toString());
		LOG.trace(":end of request.reader:");

		return new StringReader(swriter.getBuffer().toString());
	}

	@Override
	public void handle(final String handlerS, Reader reader, final Writer writer) throws IOException {

		if (LOG.isTraceEnabled()) {
			reader = traceReader(reader);
		}

		Reader readerF = reader;

		StringWriter writerF = new StringWriter();
		handlers.handle(handlerS, readerF, writerF);
		if (LOG.isTraceEnabled()) {
			LOG.trace(":start of response.writer:");
			LOG.trace(writerF.getBuffer().toString());
			LOG.trace(":end of response.writer:");
		}

		writer.write(writerF.getBuffer().toString());

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

}
