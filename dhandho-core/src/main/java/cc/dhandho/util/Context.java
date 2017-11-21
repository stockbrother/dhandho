package cc.dhandho.util;

public abstract class Context {

	public abstract <T> T get(Class<T> cls);

}
