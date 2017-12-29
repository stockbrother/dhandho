package cc.dhandho;

/**
 * 
 * @author Wu
 *
 */

public abstract class AppContext {
	
	@Deprecated // use XxxContext to reference a app context and find any component..
	public static interface Aware {
		public void setAppContext(AppContext app);
	}

	// public abstract void execute(Handler handler);

	public abstract void destroy();

	public abstract <T> T newInstance(Class<T> cls);

	public abstract <T> void addComponent(Class<T> cls, T obj);

	public abstract <T> T findComponent(Class<T> cls, boolean force);

}
