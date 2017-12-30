package cc.dhandho.commons.container;

/**
 * 
 * @author Wu
 *
 */

public interface Container {

	public static interface Aware {
		public void setContainer(Container app);
	}

	public void destroy();

	/**
	 * Create a object, set the container if the object support Aware interface.
	 * 
	 * @param cls
	 * @return
	 */
	public <T> T newInstance(Class<T> cls);

	/**
	 * Add component with the type declared.
	 * 
	 * @param cls
	 * @param obj
	 * @return
	 */
	public <T> Container addComponent(Class<T> cls, T obj);

	/**
	 * Find a component that support the type.
	 * 
	 * @param cls
	 * @param force
	 * @return
	 */
	public <T> T findComponent(Class<T> cls, boolean force);

}
