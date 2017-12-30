package cc.dhandho.commons.container;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.dhandho.RtException;

public class ContainerImpl implements Container {
	private static final Logger LOG = LoggerFactory.getLogger(ContainerImpl.class);

	// private static ThreadLocal<AppContextImpl> THREAD_LOCAL = new
	// ThreadLocal<>();

	// private GDBTemplate dbTemplate;
	private Map<Class, Object> componentMap = new HashMap<>();

	public ContainerImpl() {

		// this.dbTemplate = new GDBTemplate();
	}

	public void destroy() {

	}
	
	@Override
	public <T> T newInstance(Class<T> cls) {
		T rt;
		try {
			rt = cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw RtException.toRtException(e);
		}

		if (rt instanceof Container.Aware) {
			((Container.Aware) rt).setContainer(this);
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
	public <T> Container addComponent(Class<T> cls, T obj) {
		Object old = this.componentMap.put(cls, obj);
		if (old != null) {
			throw new RtException("duplicated component:" + old);
		}
		return this;
	}

}
