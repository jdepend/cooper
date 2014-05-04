package jdepend.model.util;

import java.util.Comparator;

import jdepend.model.JDependUnit;

/**
 * 按类型比较JDependUnit的比较器
 * 
 * @author <b>Abner</b>
 * 
 */
public class JDependUnitTypeComparator implements Comparator {

	public int compare(Object p1, Object p2) {

		JDependUnit a = (JDependUnit) p1;
		JDependUnit b = (JDependUnit) p2;

		Object atype = a.getType();
		Object btype = b.getType();

		if (atype == null)
			return 1;
		if (btype == null)
			return -1;

		int rtn = a.getType().getName().compareTo(b.getType().getName());

		if (rtn == 0) {
			return a.getName().compareTo(b.getName());
		} else {
			return rtn;
		}
	}

}
