package jdepend.model;

import java.io.Serializable;

import jdepend.framework.util.MetricsFormat;

/**
 * 关系元素
 * 
 * @author <b>Abner</b>
 * 
 */
public final class Element implements Serializable, Comparable<Element> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4123313621635544844L;

	private Component component = null;

	public Element(Component component) {
		this.component = component;
	}

	/**
	 * 元素内聚值
	 * 
	 * @return
	 */
	public float getIntensity() {
		return component.cohesion();
	}

	/**
	 * 元素稳定性
	 * 
	 * @return
	 */
	public float getInstability() {
		return component.stability();
	}

	/**
	 * 元素内聚性指数
	 * 
	 * @return
	 */
	public float getBalance() {
		return component.balance();
	}

	public String getName() {
		return this.component.getName();
	}

	public Component getComponent() {
		return component;
	}

	public int size() {
		return this.component.getClassCount();
	}

	@Override
	public String toString() {
		StringBuilder info = new StringBuilder();
		if (this.component.getAreaComponent() != null) {
			info.append(this.component.getAreaComponent().getName());
			info.append(" ");
		} else {
			String layerDesc = this.component.getLayerDesc();
			if (layerDesc != null) {
				info.append(layerDesc);
				info.append(" ");
			}
		}

		info.append("内聚性指数：");
		info.append(MetricsFormat.toFormattedMetrics(this.getBalance()));
		info.append("(");
		info.append(getBalance(this.getBalance()));
		info.append(") 稳定性：");
		info.append(MetricsFormat.toFormattedMetrics(this.getInstability()));
		info.append("(");
		info.append(getInstability(this.getInstability()));
		info.append(")");

		return info.toString();
	}

	private static String getBalance(float instability) {
		if (instability < 0.33) {
			return "较差";
		} else if (instability > 0.66) {
			return "较好";
		} else {
			return "中等";
		}
	}

	private static String getInstability(float instability) {
		if (instability < 0.33) {
			return "稳定";
		} else if (instability > 0.66) {
			return "不稳定";
		} else {
			return "中等";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((component == null) ? 0 : component.hashCode());
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
		final Element other = (Element) obj;
		if (component == null) {
			if (other.component != null)
				return false;
		} else if (!component.equals(other.component))
			return false;
		return true;
	}

	@Override
	public int compareTo(Element o) {
		// 按Ca值从小到大排序
		int rtn = (new Integer(this.component.afferentCoupling()).compareTo(o.component.afferentCoupling()));

		if (rtn != 0) {
			return rtn;
		} else {
			// 按Ce值从大到小
			rtn = (new Integer(o.component.efferentCoupling()).compareTo(this.component.efferentCoupling()));
			if (rtn != 0) {
				return rtn;
			} else {
				// 按抽象程度从小到大
				return (new Float(this.component.abstractness()).compareTo(o.component.abstractness()));
			}
		}
	}
}
