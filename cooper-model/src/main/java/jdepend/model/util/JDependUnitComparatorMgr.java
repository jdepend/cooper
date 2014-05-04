package jdepend.model.util;

import java.util.Comparator;

public class JDependUnitComparatorMgr {

	private static Comparator comparator = new JDependUnitByMetricsComparator();

	public static Comparator getComparator() {
		return comparator;
	}

	public static void setComparator(Comparator comparator) {
		JDependUnitComparatorMgr.comparator = comparator;
	}

}
