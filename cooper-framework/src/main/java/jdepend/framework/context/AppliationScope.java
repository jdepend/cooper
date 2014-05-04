package jdepend.framework.context;

import java.util.Hashtable;
import java.util.Map;

public class AppliationScope implements Scope {

	private static Map<String, Object> context = new Hashtable<String, Object>();

	public Object getInfo(String key) {
		return context.get(key);
	}

	public void setInfo(String key, Object value) {
		if (value == null) {
			context.remove(key);
		} else {
			context.put(key, value);
		}
	}

}
