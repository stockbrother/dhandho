package cc.dhandho.commons.lang;

public interface Provider {
	public <T> T get(Class<T> cls);
}
