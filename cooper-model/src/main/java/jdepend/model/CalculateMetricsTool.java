package jdepend.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import jdepend.framework.util.MathUtil;
import jdepend.model.relationtype.JavaClassRelationTypeMgr;

/**
 * 计算计算得到的指标的工具 仅仅在model中使用
 * 
 * @author <b>Abner</b>
 * 
 */
public final class CalculateMetricsTool {

	private JDependUnit unit = null;

	public static float stabilityWithCountScale = 0.5F;// stability数量所占比例（0-1）

	public CalculateMetricsTool(JDependUnit unit) {
		this.unit = unit;
	}

	/**
	 * @return Stability (0-1).
	 */
	public float stabilityWithCount() {
		int ce = unit.getEfferentCoupling();
		int ca = unit.getAfferentCoupling();
		int totalCoupling = ce + ca;
		if (totalCoupling > 0) {
			return (float) ce / (float) totalCoupling;
		} else {
			return 0.5F;
		}
	}

	/**
	 * @return Instability (0-1).
	 */
	public float stabilityWithIntensity() {
		if (!MathUtil.isZero(unit.getCoupling())) {
			return unit.ceCoupling() / unit.getCoupling();
		} else {
			return 0.5F;
		}
	}

	float stability() {

		return stabilityWithCount() * stabilityWithCountScale + stabilityWithIntensity()
				* (1 - stabilityWithCountScale);

	}

	/**
	 * 
	 * @return volatility (0-1).
	 */
	float volatility() {
		if (unit.getClassCount() > 0) {
			int stability = 0;
			for (JavaClass javaClass : unit.getClasses()) {
				if (javaClass.isStable()) {
					stability++;
				}
			}
			return stability * 1F / unit.getClassCount();
		} else {
			return 0F;
		}
	}

	/**
	 * @return The package's abstractness (0-1).
	 */
	float abstractness() {
		if (unit.getClassCount() > 0) {
			return (float) unit.getAbstractClassCount() / (float) unit.getClassCount();
		}
		return 0;
	}

	/**
	 * @return The package's distance from the main sequence (D).
	 */
	float distance() {
		if (this.unit.getAfferents().size() == 0 && this.unit.getEfferents().size() == 0) {
			return 0.5F;
		} else {
			return Math.abs(abstractness() + volatility() + stability() - 1);
		}
	}

	/**
	 * 计算未被其他组件使用的类的比例
	 * 
	 * @param javaClasses
	 * @return
	 */
	public Float encapsulation() {
		if (this.unit.getAfferentCoupling() == 0) {
			return null;
		} else {
			int privates = 0;
			for (JavaClass javaClass : this.unit.getClasses()) {
				if (!javaClass.isUsedByExternal()) {
					privates += 1;
				}
			}
			return privates * 1F / this.unit.getClasses().size();
		}
	}

	/**
	 * 计算基于数据库表实现组件间通讯的比重
	 * 
	 * @param javaClasses
	 * @return
	 */
	public static Float tableRelationScale(Collection<JavaClass> javaClasses) {
		if (javaClasses != null && javaClasses.size() > 0) {
			int tableRelations = 0;
			int relations = 0;
			for (JavaClass javaClass : javaClasses) {
				for (JavaClassRelationItem item : javaClass.getCeItems()) {
					if (!item.isInner()) {
						if (item.getType().equals(JavaClassRelationTypeMgr.getInstance().getTableRelation())) {
							tableRelations += 1;
						}
						relations += 1;
					}
				}
			}
			if (relations == 0) {
				return 0.0F;
			} else {
				return tableRelations * 1F / relations;
			}
		} else {
			return 0.0F;
		}
	}

	/**
	 * 返回循环依赖链
	 * 
	 * @return
	 */
	List<List<? extends JDependUnit>> collectCycle() {
		return (new OnlyOneCycleIdentifyer()).collectCycle(unit);
	}
}
