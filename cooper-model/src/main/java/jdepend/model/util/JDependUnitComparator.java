package jdepend.model.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jdepend.model.JDependUnit;

public final class JDependUnitComparator implements Comparator {

	private JDependUnit a;
	private JDependUnit b;

	private List<JDependUnit> list;

	@Override
	public int compare(Object arg0, Object arg1) {
		a = (JDependUnit) arg0;
		b = (JDependUnit) arg1;

		list = new ArrayList<JDependUnit>();
		if (this.compareCa(a) == 1) {
			return 1;
		}

		list = new ArrayList<JDependUnit>();
		if (this.compareCe(a) == -1) {
			return -1;
		}

		return 0;
	}

	private int compareCa(JDependUnit current) {

		if (list.contains(current)) {
			return -2;
		} else {
			list.add(current);
		}
		for (JDependUnit unit : current.getAfferents()) {
			if (unit.equals(b)) {
				return 1;
			}
		}
		for (JDependUnit unit : current.getAfferents()) {
			int rtn = this.compareCa(unit);
			if (rtn == 1) {
				return 1;
			} else if (rtn == -2) {
				continue;
			}

		}
		return 0;
	}

	private int compareCe(JDependUnit current) {

		if (list.contains(current)) {
			return -2;
		} else {
			list.add(current);
		}
		for (JDependUnit unit : current.getEfferents()) {
			if (unit.equals(b)) {
				return -1;
			}
		}
		for (JDependUnit unit : current.getEfferents()) {
			int rtn = this.compareCe(unit);
			if (rtn == -1) {
				return -1;
			} else if (rtn == -2) {
				continue;
			}
		}
		return 0;
	}

}
