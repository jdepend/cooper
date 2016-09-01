package jdepend.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收集第一个识别的循环依赖链（其他的不识别）
 * 
 * @author wangdg
 * 
 */
public final class OnlyOneCycleIdentifyer implements CycleIdentifyer {

	@Override
	public List<List<? extends JDependUnit>> collectCycle(JDependUnit unit) {
		List<List<? extends JDependUnit>> cycles = new ArrayList<List<? extends JDependUnit>>();
		List<JDependUnit> cycle = new ArrayList<JDependUnit>();
		Map<JDependUnit, Integer> knowledge = new HashMap<JDependUnit, Integer>();

		if (unit.collectCycle(cycle, knowledge) == Cycle) {
			cycles.add(cycle);
			return cycles;
		} else {
			return null;
		}
	}

}
