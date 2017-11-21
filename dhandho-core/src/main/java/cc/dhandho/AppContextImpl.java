package cc.dhandho;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;

import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.graphdb.GDBTemplate;
import cc.dhandho.graphdb.Handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class AppContextImpl extends AppContext implements Thread.UncaughtExceptionHandler, ThreadFactory {
	private static final Logger LOG = LoggerFactory.getLogger(AppContextImpl.class);

	private static ThreadLocal<AppContextImpl> THREAD_LOCAL = new ThreadLocal<>();

	private ScheduledExecutorService executor;

	private OrientDB orient;

	private GDBTemplate dbTemplate;

	private DbConfig dbConfig;

	public ScheduledExecutorService getExecutor() {
		return this.executor;
	}

	public void execute(Handler handler) {
		this.executor.execute(new Runnable() {
			@Override
			public void run() {
				handler.execute();
			}
		});
	}

	public void executeSync(Handler handler) {
		AppContextImpl old = THREAD_LOCAL.get();
		THREAD_LOCAL.set(this);
		try {
			handler.execute();
		} finally {
			THREAD_LOCAL.set(old);
		}

	}

	public AppContextImpl dbConfig(DbConfig dbConfig) {
		this.dbConfig = dbConfig;
		return this;
	}

	public AppContextImpl create() {
		this.executor = Executors.newScheduledThreadPool(1, this);
		orient = this.dbConfig.createDb();
		this.dbTemplate = new GDBTemplate();
		return this;
	}

	public GDBTemplate getDbTemplate() {
		return this.dbTemplate;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		LOG.error("uncaughtException in thread:" + thread.getName(), throwable);
	}

	@Override
	public Thread newThread(Runnable r) {
		return new AppThread(r, this);
	}

	public static class AppThread extends Thread {
		AppContextImpl ddr;

		public AppThread(Runnable r, AppContextImpl ddr) {
			super(r);
			this.ddr = ddr;
			this.setUncaughtExceptionHandler(ddr);
		}

		@Override
		public void run() {
			AppContextImpl.set(this.ddr);
			super.run();
		}
	}

	public static AppContextImpl get() {
		return THREAD_LOCAL.get();
	}

	public void destroy() {
		this.executor.shutdown();
		try {
			this.executor.awaitTermination(100, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			throw new RuntimeException("", e);
		}
	}

	public ODatabaseSession openDB() {
		// TODO avoid openDB twice.
		// TODO if dbName = null, it will block here. why?
		ODatabaseSession databaseSession = this.dbConfig.openDb(orient, true);
		return databaseSession;
	}

	public static AppContextImpl set(AppContextImpl app) {
		AppContextImpl rt = THREAD_LOCAL.get();
		THREAD_LOCAL.set(app);
		return rt;
	}

	@Override
	public <T> T newInstance(Class<T> cls) {
		T rt;
		try {
			rt = cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw RtException.toRtException(e);
		}

		if (rt instanceof AppContext.Aware) {
			((AppContext.Aware) rt).setAppContext(this);
		}
		return rt;
	}

}
