package jdepend.model;

import jdepend.framework.util.MathUtil;

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
	public Float stabilityWithCount() {
		int ce = unit.getEfferentCoupling();
		int ca = unit.getAfferentCoupling();
		int totalCoupling = ce + ca;
		if (totalCoupling > 0) {
			return (float) ce / (float) totalCoupling;
		} else {
			return null;
		}
	}

	/**
	 * @return Instability (0-1).
	 */
	public Float stabilityWithIntensity() {
		if (!MathUtil.isZero(unit.getCoupling())) {
			return unit.ceCoupling() / unit.getCoupling();
		} else {
			return null;
		}
	}

	/**
	 * 稳定性
	 * 
	 * @return
	 */
	public Float stability() {
		if (MathUtil.isZero(unit.getCoupling())) {
			return null;
		} else {
			return stabilityWithCount() * stabilityWithCountScale + stabilityWithIntensity()
					* (1 - stabilityWithCountScale);
		}
	}

	/**
	 * 
	 * 易变性
	 */
	public float volatility() {
		if (unit.getClassCount() > 0) {
			int stability = 0;
			for (JavaClassUnit javaClass : unit.getClasses()) {
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
	 * 抽象性
	 */
	public float abstractness() {
		if (unit.getClassCount() > 0) {
			return (float) unit.getAbstractClassCount() / (float) unit.getClassCount();
		}
		return 0;
	}

	/**
	 * 抽象程度合理性
	 */
	public Float distance() {
		if (this.unit.getAfferents().size() == 0 && this.unit.getEfferents().size() == 0) {
			return null;
		} else {
			return Math.abs(abstractness() + stability() - 1);
		}
	}

	/**
	 * 封装性
	 * 
	 * @param javaClasses
	 * @return
	 */
	public Float encapsulation() {
		if (this.unit.getAfferentCoupling() == 0) {
			return null;
		} else {
			int privates = 0;
			// int count = 0;
			// for (JavaClass javaClass : this.unit.getClasses()) {
			// if (javaClass.getClassType().equals(JavaClass.Service_TYPE)) {
			// count += 1;
			// if (!javaClass.isUsedByExternal()) {
			// privates += 1;
			// }
			// }
			// }
			// if (count > 0) {
			// return privates * 1F / count;
			// } else {
			// return 1F;
			// }

			for (JavaClassUnit javaClass : this.unit.getClasses()) {
				if (!javaClass.isUsedByExternal()) {
					privates += 1;
				}
			}
			return privates * 1F / this.unit.getClassCount();
		}
	}
}
