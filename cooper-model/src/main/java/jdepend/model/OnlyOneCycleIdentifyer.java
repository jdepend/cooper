package jdepend.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
	public List<List<JDependUnit>> collectCycle(JDependUnit unit) {
		List<List<JDependUnit>> cycles = new ArrayList<List<JDependUnit>>();
		List<JDependUnit> cycle = new ArrayList<JDependUnit>();
		Map<JDependUnit, Integer> knowledge = new HashMap<JDependUnit, Integer>();
		if (this.collectCycle(unit, cycle, knowledge) == Cycle) {
			cycles.add(cycle);
			return cycles;
		} else {
			return null;
		}
	}

	@Override
	public int collectCycle(JDependUnit unit, List<JDependUnit> list, Map<JDependUnit, Integer> knowledge) {

		if (list.contains(unit)) {
			if (list.get(0).equals(unit)) {
				return Cycle;// 存在循环依赖
			} else {
				knowledge.put(unit, LocalCycle);
				return LocalCycle;// 存在局部循环依赖
			}
		}

		list.add(unit);// 将当前分析单元入栈

		if (unit.getEfferents().contains(list.get(0))) {// 直接依赖进行广度搜索
			return Cycle;
		}

		for (Iterator<? extends JDependUnit> i = unit.getEfferents().iterator(); i.hasNext();) {
			JDependUnit efferent = i.next();
			Integer rtnInteger = (Integer) knowledge.get(efferent);// 获取历史扫描数据
			if (rtnInteger == null) {// 没有扫描过的区域进行深度扫描
				int rtn = efferent.collectCycle(list, knowledge);// 深度搜索该区域
				if (rtn == Cycle) {// 存在循环依赖
					return Cycle;
				}
			}
		}

		list.remove(unit);// 将当前分析单元出栈

		knowledge.put(unit, NoCycle);// 记录该对象扫描过的结果

		return NoCycle;// 不存在循环依赖
	}

}
