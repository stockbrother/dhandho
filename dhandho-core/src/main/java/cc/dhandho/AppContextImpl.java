package cc.dhandho;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppContextImpl extends AppContext {
	private static final Logger LOG = LoggerFactory.getLogger(AppContextImpl.class);

	// private static ThreadLocal<AppContextImpl> THREAD_LOCAL = new
	// ThreadLocal<>();

	// private GDBTemplate dbTemplate;
	private Map<Class, Object> componentMap = new HashMap<>();

	public AppContextImpl() {

		// this.dbTemplate = new GDBTemplate();
	}

	// public ScheduledExecutorService getExecutor() {
	// return this.executor;
	// }
	//
	// public void execute(Handler handler) {
	// this.executor.execute(new Runnable() {
	// @Override
	// public void run() {
	// handler.execute();
	// }
	// });
	// }
	//
	// public void executeSync(Handler handler) {
	// AppContextImpl old = THREAD_LOCAL.get();
	// THREAD_LOCAL.set(this);
	// try {
	// handler.execute();
	// } finally {
	// THREAD_LOCAL.set(old);
	// }
	//
	// }

	// public GDBTemplate getDbTemplate() {
	// return this.dbTemplate;
	// }

	public void destroy() {

	}
	//
	// public static AppContextImpl set(AppContextImpl app) {
	// AppContextImpl rt = THREAD_LOCAL.get();
	// THREAD_LOCAL.set(app);
	// return rt;
	// }

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

	@Override
	public <T> T findComponent(Class<T> cls, boolean force) {
		Object rt = this.componentMap.get(cls);
		if (rt == null && force) {
			throw new RtException("no component found for cls:" + cls);
		}
		return (T) rt;
	}

	@Override
	public <T> void addComponent(Class<T> cls, T obj) {
		Object old = this.componentMap.put(cls, obj);
		if (old != null) {
			throw new RtException("duplicated component:" + old);
		}
	}

}
