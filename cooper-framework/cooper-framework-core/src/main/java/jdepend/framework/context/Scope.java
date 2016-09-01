package jdepend.framework.context;

public interface Scope {

	public void setInfo(String key, Object value);

	public Object getInfo(String key);

	public enum SCOPE {
		APP_SCOPSE, THREAD_SCOPSE;
	}
}
