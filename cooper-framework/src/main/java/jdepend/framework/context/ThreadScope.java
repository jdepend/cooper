package jdepend.framework.context;

import java.util.HashMap;
import java.util.Map;

public class ThreadScope implements Scope {

	private static ThreadLocal<Map<String, Object>> context = new ThreadLocal<Map<String, Object>>();

	public void setInfo(String key, Object value) {
		Map<String, Object> data = context.get();
		if (data == null) {
			data = new HashMap<String, Object>();
			context.set(data);
		}
		if (value == null) {
			data.remove(key);
		} else {
			data.put(key, value);
		}
	}

	public Object getInfo(String key) {
		Map<String, Object> data = context.get();
		if (data != null)
			return data.get(key);
		else
			return null;
	}

}
