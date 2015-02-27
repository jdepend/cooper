package jdepend.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ComponentPathSegment implements Comparable<ComponentPathSegment> {

	private String name;
	private Integer count;

	public ComponentPathSegment(String name) {
		super();
		this.name = name;
		this.count = 1;
	}

	public String getName() {
		return name;
	}

	public Integer getCount() {
		return count;
	}

	public void addCount() {
		this.count++;
	}
	
	/**
	 * 对path集合中segment计数
	 * 
	 * @param paths
	 * @return
	 */
	public static List<ComponentPathSegment> create(Collection<String> paths) {

		List<ComponentPathSegment> segments = new ArrayList<ComponentPathSegment>();
		ComponentPathSegment st1 = null;
		boolean exist;
		for (String path : paths) {
			for (String segment : path.split("\\.")) {
				exist = false;
				L: for (ComponentPathSegment st : segments) {
					if (st.getName().equals(segment)) {
						st1 = st;
						exist = true;
						break L;
					}
				}
				if (exist) {
					st1.addCount();
				} else {
					segments.add(new ComponentPathSegment(segment));
				}
			}
		}

		Collections.sort(segments);

		return segments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComponentPathSegment other = (ComponentPathSegment) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(ComponentPathSegment o) {
		return o.count.compareTo(this.count);
	}
}