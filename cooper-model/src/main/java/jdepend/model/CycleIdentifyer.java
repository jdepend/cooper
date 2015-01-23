package jdepend.model;

import java.util.List;

/**
 * 循环依赖识别器
 * 
 * @author wangdg
 * 
 */
public interface CycleIdentifyer {

	public final static int Cycle = 2;
	public final static int LocalCycle = 1;
	public final static int NoCycle = 0;

	/**
	 * 返回循环依赖链
	 * 
	 * @param unit
	 * @return
	 */
	public List<List<? extends JDependUnit>> collectCycle(JDependUnit unit);
}
