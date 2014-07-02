package jdepend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jdepend.framework.util.MathUtil;
import jdepend.framework.util.MetricsFormat;

/**
 * 两个Element间的关系对象
 * 
 * @author <b>Abner</b>
 * 
 */
public final class Relation implements Comparable<Relation>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6078087861336926293L;

	private Element current = null;

	private Element depend = null;

	private float intensity = 0F;

	private transient boolean isAttention = true;

	// 仅包含CeItem
	private transient Collection<JavaClassRelationItem> items;// 缓存

	private transient Integer attentionType;// 缓存
	private transient Float attentionLevel;// 缓存

	public static final int DefaultAttentionType = 0;
	public static final int CycleDependAttentionType = 1;// 循环依赖的关系
	public static final int SDPAttentionType = 2;// 违反稳定依赖原则的关系
	public static final int ComponentLayerAttentionType = 3;// 下层组件依赖了上层组件的关系
	public static final int MutualDependAttentionType = 4;// 彼此依赖的关系

	public static final Map<Integer, String> AttentionTypeList = new HashMap<Integer, String>();
	static {
		AttentionTypeList.put(DefaultAttentionType, "");
		AttentionTypeList.put(SDPAttentionType, "违反稳定依赖原则");
		AttentionTypeList.put(ComponentLayerAttentionType, "下层组件依赖了上层组件");
		AttentionTypeList.put(CycleDependAttentionType, "循环依赖");
		AttentionTypeList.put(MutualDependAttentionType, "彼此依赖");
	}

	public static final String Intensity = "Intensity";
	public static final String AttentionType = "AttentionType";
	public static final String AttentionLevel = "AttentionLevel";
	public static final String Balance = "Balance";

	public void setCurrent(Element current) {
		this.current = current;
		this.current.getComponent().addRelation(this);
	}

	public void setDepend(Element depend) {
		this.depend = depend;
		this.depend.getComponent().addRelation(this);
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	/**
	 * 关系平衡指数
	 * 
	 * @return
	 */
	public float getBalance() {
		return this.current.getIntensity() + this.depend.getIntensity() - intensity;
	}

	public int size() {
		return this.getItems().size();
	}

	public int compareTo(Relation obj) {
		return new Float(this.getBalance()).compareTo(obj.getBalance());
	}

	public Element getCurrent() {
		return current;
	}

	public Element getDepend() {
		return depend;
	}

	public Collection<JavaClassRelationItem> getItems() {
		if (this.items == null) {
			this.items = current.getComponent().calCeCouplingDetail(this.depend.getComponent());
		}
		return items;
	}

	public int getAttentionType() {
		if (!this.isAttention) {
			return DefaultAttentionType;
		}
		if (this.attentionType == null) {
			this.attentionType = this.calAttentionType();
		}
		return this.attentionType;
	}

	/**
	 * 得到关注程度
	 * 
	 * @param relations
	 * @return
	 */
	public Float getAttentionLevel() {
		if (!this.isAttention) {
			return new Float(DefaultAttentionType);
		}
		if (this.attentionLevel == null) {
			int attentiontype = getAttentionType();
			if (attentiontype == MutualDependAttentionType) {// 彼此依赖
				for (Relation relation : this.depend.getComponent().getRelations()) {
					if (relation.getCurrent().equals(this.depend) && relation.getDepend().equals(this.current)) {
						if (MathUtil.isEquals(relation.getIntensity(), this.getIntensity())) {
							this.attentionLevel = new Float(attentiontype);
						} else {
							Float attention = 1 - this.getIntensity() / (this.getIntensity() + relation.getIntensity());
							this.attentionLevel = attentiontype + attention;
						}
					}
				}
			} else if (attentiontype == SDPAttentionType) {// 稳定性差的组件依赖稳定性高的组件
				Float attention = this.getDepend().getComponent().getStability()
						- this.getCurrent().getComponent().getStability();
				if (attention > 0) {
					this.attentionLevel = attentiontype + attention;// 按自动计算的稳定性计算attentionLevel
				} else if (this.current.getComponent().getSteadyType() != null
						&& this.depend.getComponent().getSteadyType() != null) {// 按人工指定的稳定性计算attentionLevel
					if (this.current.getComponent().getSteadyType().equals(Component.StableType)
							&& this.depend.getComponent().getSteadyType().equals(Component.MiddleType)
							|| this.current.getComponent().getSteadyType().equals(Component.MiddleType)
							&& this.depend.getComponent().getSteadyType().equals(Component.MutabilityType)) {
						this.attentionLevel = attentiontype + 0.1F;
					} else if (this.current.getComponent().getSteadyType().equals(Component.StableType)
							&& this.depend.getComponent().getSteadyType().equals(Component.MutabilityType)) {
						this.attentionLevel = attentiontype + 0.2F;
					}
				}
			} else if (attentiontype == ComponentLayerAttentionType) {// 下层组件依赖了上层组件
				Float attention = 0F;
				if (this.current.getComponent().getAreaComponent() != null
						&& this.depend.getComponent().getAreaComponent() != null) {
					attention = this.getDepend().getComponent().getAreaComponent().instability()
							- this.getCurrent().getComponent().getAreaComponent().instability();
				} else {
					attention = new Float(this.getDepend().getComponent().getLayer()
							- this.getCurrent().getComponent().getLayer());
				}
				this.attentionLevel = attentiontype + attention;
			} else {
				this.attentionLevel = new Float(attentiontype);
			}
		}
		return this.attentionLevel;
	}

	public String getAttentionTypeName() {
		return AttentionTypeList.get(this.getAttentionType());
	}

	public boolean isAttention() {
		return this.isAttention && this.getAttentionType() != DefaultAttentionType;
	}

	public void setAttention(boolean isAttention) {
		this.isAttention = isAttention;
	}

	public Object getValue(String metrics) {
		if (metrics.equals(MetricsMgr.Relation_CurrentName)) {
			return this.current.getName();
		} else if (metrics.equals(MetricsMgr.Relation_DependName)) {
			return this.depend.getName();
		} else if (metrics.equals(MetricsMgr.Relation_CurrentCohesion)) {
			return MetricsFormat.toFormattedMetrics(this.current.getIntensity());
		} else if (metrics.equals(MetricsMgr.Relation_DependCohesion)) {
			return MetricsFormat.toFormattedMetrics(this.depend.getIntensity());
		} else if (metrics.equals(MetricsMgr.Relation_Balance)) {
			return MetricsFormat.toFormattedMetrics(this.getBalance());
		} else if (metrics.equals(MetricsMgr.Relation_Intensity)) {
			return MetricsFormat.toFormattedMetrics(this.getIntensity());
		} else if (metrics.equals(MetricsMgr.Relation_AttentionType)) {
			return Relation.AttentionTypeList.get(this.getAttentionType());
		} else if (metrics.equals(MetricsMgr.Relation_AttentionLevel)) {
			return this.getAttentionLevel();
		} else {
			return null;
		}
	}

	private int calAttentionType() {
		if (this.depend.getComponent().getEfferents().contains(this.current.getComponent())) {// 检测彼此依赖
			return MutualDependAttentionType;
		} else if (this.current.getComponent().isDefinedComponentLevel()
				&& this.depend.getComponent().isDefinedComponentLevel()
				&& this.current.getComponent().getLayer() < this.depend.getComponent().getLayer()) {// 检测组件层依赖(人工指定)
			return ComponentLayerAttentionType;
		} else if (this.current.getComponent().getAreaComponent() != null
				&& this.depend.getComponent().getAreaComponent() != null
				&& this.current.getComponent().getAreaComponent().instability() < this.depend.getComponent()
						.getAreaComponent().instability()) {// 检测组件层依赖（按着AreaComponent）
			return ComponentLayerAttentionType;
		} else if (this.current.getComponent().stability(this.depend.getComponent())) {// 检测稳定依赖（按着自动计算的稳定性）
			return SDPAttentionType;
		} else if (this.current.getComponent().getSteadyType() != null
				&& this.depend.getComponent().getSteadyType() != null
				&& (this.current.getComponent().getSteadyType().equals(Component.StableType)
						&& (this.depend.getComponent().getSteadyType().equals(Component.MiddleType) || this.depend
								.getComponent().getSteadyType().equals(Component.MutabilityType)) || this.current
						.getComponent().getSteadyType().equals(Component.MiddleType)
						&& this.depend.getComponent().getSteadyType().equals(Component.MutabilityType))) {
			return SDPAttentionType;
		} else if (this.current.getComponent().collectCycle().contains(this.depend.getComponent())) {// 检测循环依赖
			return CycleDependAttentionType;
		} else {
			return DefaultAttentionType;
		}
	}

	public static Collection<Element> calElements(Collection<Relation> relations) {
		Collection<Element> elements = new ArrayList<Element>();
		for (Relation relation : relations) {
			if (!elements.contains(relation.getCurrent())) {
				elements.add(relation.getCurrent());
			}
			if (!elements.contains(relation.getDepend())) {
				elements.add(relation.getDepend());
			}
		}
		return elements;
	}

	public void clear() {
		this.attentionLevel = null;
		this.attentionType = null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((current == null) ? 0 : current.hashCode());
		result = prime * result + ((depend == null) ? 0 : depend.hashCode());
		return result;
	}

	public boolean equals(String current, String depend) {
		return this.current.getName().equals(current) && this.depend.getName().equals(depend);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Relation other = (Relation) obj;
		if (current == null) {
			if (other.current != null)
				return false;
		} else if (!current.equals(other.current))
			return false;
		if (depend == null) {
			if (other.depend != null)
				return false;
		} else if (!depend.equals(other.depend))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "关系耦合值：" + MetricsFormat.toFormattedMetrics(this.getIntensity()) + " " + this.getAttentionTypeName();
	}
}
