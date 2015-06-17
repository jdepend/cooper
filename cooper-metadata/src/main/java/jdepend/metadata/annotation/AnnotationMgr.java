package jdepend.metadata.annotation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class AnnotationMgr<T> {

	private static final AnnotationMgr mgr = new AnnotationMgr();

	private Map<String, Collection<T>> elements;

	private AnnotationMgr() {
	}

	public static AnnotationMgr getInstance() {
		return mgr;
	}

	public synchronized T getType(T t) {
		if (elements == null) {
			elements = new HashMap<String, Collection<T>>();
		}
		Collection<T> types = elements.get(t.getClass().getName());
		if (types == null) {
			types = new HashSet<T>();
			elements.put(t.getClass().getName(), types);
		}
		if (!types.contains(t)) {
			types.add(t);
		}
		T t1;
		Iterator<T> it = types.iterator();
		while (it.hasNext()) {
			t1 = it.next();
			if (t1.equals(t)) {
				return t1;
			}
		}
		return null;
	}

	public void reset() {
		elements = null;
	}

}
