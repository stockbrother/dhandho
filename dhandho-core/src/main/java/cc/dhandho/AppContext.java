package cc.dhandho;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.graphdb.GDBTemplate;
import cc.dhandho.graphdb.Handler;

public abstract class AppContext {
	public static interface Aware {
		public void setAppContext(AppContext app);
	}

	public abstract void execute(Handler handler);

	public abstract void executeSync(Handler handler);

	public abstract GDBTemplate getDbTemplate();

	public abstract void destroy();

	public abstract ODatabaseSession openDB();

	public abstract <T> T newInstance(Class<T> cls);

}
