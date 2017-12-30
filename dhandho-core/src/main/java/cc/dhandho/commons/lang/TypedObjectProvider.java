package cc.dhandho.commons.lang;

public interface TypedObjectProvider {
	public <T> T resolve(Class<T> cls);
}
