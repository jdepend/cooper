package jdepend.knowledge.domainanalysis.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jdepend.model.Component;
import jdepend.model.MetricsMgr;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JDependUnitByMetricsComparator;

public class CohesionUtil {

	public static List<Component> sort(AnalysisResult result) {
		List<Component> list = new ArrayList<Component>();
		List<Component> components = result.getComponents();
		Collections.sort(components, new JDependUnitByMetricsComparator(MetricsMgr.Balance));
		Iterator<Component> it = components.iterator();
		Component component = null;
		while (it.hasNext()) {
			component = it.next();
			if (component.isInner() && component.getClassCount() > 1) {
				list.add(component);
			}
		}
		return list;
	}

}
