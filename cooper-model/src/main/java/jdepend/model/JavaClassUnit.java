package jdepend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import jdepend.framework.log.LogUtil;
import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.metadata.JavaPackage;
import jdepend.model.result.AnalysisResult;

/**
 * 一个JavaClassUnit代表作为评价体系中的一个单元.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class JavaClassUnit extends AbstractSubJDependUnit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3619193239851101008L;

	private JavaClass javaClass;

	private Component component;

	private List<JavaClassUnit> innerClassUnits = new ArrayList<JavaClassUnit>();

	private Boolean abstractClassQualification;// 是否具有抽象类资格
	private boolean stable = false;

	private transient Collection<JavaClassUnit> caList = null;
	private transient Collection<JavaClassUnit> ceList = null;
	private transient Collection<JavaClassUnit> relationList = null;
	private transient Collection<JavaClassUnit> afferents = null;
	private transient Collection<JavaClassUnit> efferents = null;

	public static final String Place = "JavaClass_Place";
	public static final String isPrivateElement = "JavaClass_isPrivateElement";
	public static final String Stable = "JavaClass_Stable";
	public static final String State = "JavaClass_State";
	public static final String ClassType = "JavaClass_Type";

	public JavaClassUnit(JavaClass javaClass) {
		this.javaClass = javaClass;

		for (JavaClass innerClass : javaClass.getInnerClasses()) {
			this.innerClassUnits.add(new JavaClassUnit(innerClass));
		}
	}

	public JavaClass getJavaClass() {
		return javaClass;
	}

	@Override
	public Collection<JavaClassUnit> getClasses() {
		Collection<JavaClassUnit> javaClasses = new ArrayList<JavaClassUnit>();
		javaClasses.add(this);
		return javaClasses;
	}

	public String getId() {
		return this.javaClass.getId();
	}

	@Override
	public String getName() {
		return this.javaClass.getName();
	}

	@Override
	public String getPath() {
		return javaClass.getPackageName();
	}

	public List<JavaClassUnit> getInnerClassUnits() {
		return innerClassUnits;
	}

	@Override
	public int getLineCount() {
		return this.javaClass.getLineCount();
	}

	@Override
	public boolean isInner() {
		return this.javaClass.isInner();
	}

	/**
	 * 是否为私有Class，与组件外部的Class没有关系
	 * 
	 * @return
	 */
	public boolean isPrivateElement() {
		return this.getAfferents().size() == 0 && this.getEfferents().size() == 0;
	}

	/**
	 * 是否被其他组件使用
	 * 
	 * @return
	 */
	public boolean isUsedByExternal() {
		return this.getAfferents().size() != 0;
	}

	/**
	 * 在组件中是否孤立，与组件中的其他Class都没有关系
	 * 
	 * @return
	 */
	public boolean isAlone() {
		for (JavaClassUnit relationClass : this.getRelationList()) {
			if (this.getComponent().containsClass(relationClass)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public AnalysisResult getResult() {
		return this.getComponent().getResult();
	}

	public int getAbstractClassCount() {
		if (this.abstractClassQualification == null) {
			return this.javaClass.isAbstract() ? 1 : 0;
		} else {
			return this.abstractClassQualification ? 1 : 0;
		}
	}

	public void setAbstractClassQualification(boolean abstractClassQualification) {
		this.abstractClassQualification = abstractClassQualification;
	}

	public int getConcreteClassCount() {
		if (getAbstractClassCount() == 1) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public float getAbstractness() {
		return this.getAbstractClassCount();
	}

	@Override
	public Collection<JavaPackage> getJavaPackages() {
		Collection<JavaPackage> javaPackages = new ArrayList<JavaPackage>();
		javaPackages.add(this.javaClass.getJavaPackage());
		return javaPackages;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
		for (JavaClassUnit innerClass : this.innerClassUnits) {
			innerClass.setComponent(component);
		}
	}

	public boolean containedComponent() {
		return this.component != null;
	}

	public void setStable(boolean b) {
		this.stable = b;
	}

	public boolean isStable() {
		return stable;
	}

	@Override
	public int getClassCount() {
		return 1;
	}

	@Override
	public boolean containsClass(JavaClassUnit javaClass) {
		return this.equals(javaClass);
	}

	@Override
	public boolean containsClass(JavaClass javaClass) {
		return this.getJavaClass().equals(javaClass);
	}

	public synchronized Collection<JavaClassUnit> getCeList() {
		if (this.ceList == null) {
			Collection<JavaClassUnit> javaClasses = new HashSet<JavaClassUnit>();
			for (JavaClassRelationItem item : this.javaClass.getCeItems()) {
				JavaClassUnit javaClassUnit = this.getResult().getTheClass(item.getTarget().getId());
				if (javaClassUnit != null) {
					javaClasses.add(javaClassUnit);
				}
			}
			this.ceList = javaClasses;
		}
		return this.ceList;
	}

	public synchronized Collection<JavaClassUnit> getCaList() {
		if (this.caList == null) {
			Collection<JavaClassUnit> javaClasses = new HashSet<JavaClassUnit>();
			for (JavaClassRelationItem item : this.javaClass.getCaItems()) {
				JavaClassUnit javaClassUnit = this.getResult().getTheClass(item.getSource().getId());
				if (javaClassUnit != null) {
					javaClasses.add(javaClassUnit);
				}
			}
			this.caList = javaClasses;
		}
		return this.caList;
	}

	public synchronized Collection<JavaClassUnit> getRelationList() {
		if (this.relationList == null) {
			Collection<JavaClassUnit> javaClasses = this.getCaList();
			for (JavaClassUnit javaClass : this.getCeList()) {
				if (!javaClasses.contains(javaClass)) {
					javaClasses.add(javaClass);
				}
			}
			this.relationList = javaClasses;
		}
		return this.relationList;
	}

	@Override
	public synchronized Collection<JavaClassUnit> getAfferents() {
		if (this.afferents == null) {
			Collection<JavaClassUnit> afferents = new HashSet<JavaClassUnit>();

			for (JavaClassUnit javaClass : this.getCaList()) {
				if (!this.component.containsClass(javaClass)) {
					afferents.add(javaClass);
				}
			}
			this.afferents = afferents;
		}
		return this.afferents;
	}

	@Override
	public synchronized Collection<JavaClassUnit> getEfferents() {
		if (this.efferents == null) {
			Collection<JavaClassUnit> efferents = new HashSet<JavaClassUnit>();

			for (JavaClassUnit javaClass : this.getCeList()) {
				if (!this.component.containsClass(javaClass)) {
					efferents.add(javaClass);
				}
			}
			this.efferents = efferents;
		}
		return this.efferents;
	}

	@Override
	public RelationDetail ceCouplingDetail(JDependUnit dependUnit) {

		RelationDetail detail = new RelationDetail();
		if (this.equals(dependUnit)) {
			return detail;
		}

		float intensity = 0;
		for (JavaClassRelationItem relationItem : this.javaClass.getCeItems()) {
			if (dependUnit.containsClass(relationItem.getTarget())) {
				detail.addItem(relationItem);
				intensity += relationItem.getRelationIntensity();
			}
		}
		detail.setIntensity(intensity);

		return detail;
	}

	@Override
	public RelationDetail caCouplingDetail(JDependUnit dependUnit) {

		RelationDetail detail = new RelationDetail();
		if (this.equals(dependUnit)) {
			return detail;
		}

		float intensity = 0;
		for (JavaClassRelationItem relationItem : this.javaClass.getCaItems()) {
			if (dependUnit.containsClass(relationItem.getSource())) {
				detail.addItem(relationItem);
				intensity += relationItem.getRelationIntensity();
			}
		}
		detail.setIntensity(intensity);

		return detail;
	}

	@Override
	public float caCoupling() {
		float intensity = 0;
		for (JavaClassRelationItem relationItem : this.javaClass.getCaItems()) {
			if (!this.component.containsClass(relationItem.getSource())) {
				intensity += relationItem.getRelationIntensity();
			}
		}
		return intensity;
	}

	@Override
	public float ceCoupling() {
		float intensity = 0;
		for (JavaClassRelationItem relationItem : this.javaClass.getCeItems()) {
			if (!this.component.containsClass(relationItem.getTarget())) {
				intensity += relationItem.getRelationIntensity();
			}
		}
		return intensity;
	}

	/**
	 * 计算在特定context环境下的内聚性
	 * 
	 * @param context
	 * @return
	 */
	@Override
	public float getCohesion() {
		return this.getGroupCohesionInfo().getCohesion();
	}

	@Override
	public Float getBalance() {
		return this.getGroupInfoCalculator().getBalance();
	}

	@Override
	protected GroupInfoCalculator createGroupInfoCalculator() {
		return new GroupInfoCalculator(this);
	}

	@Override
	public int collectCycle(List<JDependUnit> list, Map<JDependUnit, Integer> knowledge) {

		if (!this.getComponent().getContainsCycle()) {
			return NoCycle;// 当类所在的组件不存在循环依赖时，类也不会存在循环依赖
		}

		if (list.size() > 20) {
			LogUtil.getInstance(JavaClassUnit.class).systemWarning(
					"JavaClass[" + list.get(0).getName() + "] [" + this.getName() + "]collectCycle 搜索深度大于20停止搜索");
			return StopCheckCycle;// 搜索深度大于20时停止
		}

		if (list.contains(this)) {
			if (list.get(0).equals(this)) {
				for (JDependUnit unit : list) {
					if (!this.component.containsClass((JavaClassUnit) unit)) {
						return Cycle;// 存在循环依赖
					}
				}
				knowledge.put(this, LocalCycle);
				return LocalCycle;// 存在局部循环依赖
			} else {
				// 通知其他组件存在循环依赖
				int index;
				L: for (index = 1; index < list.size(); index++) {
					if (list.get(index).equals(this)) {
						break L;
					}
				}
				List<JavaClassUnit> otherCycles = new ArrayList<JavaClassUnit>();
				Collection<Component> otherCycleComponents = new HashSet<Component>();
				for (int pos = index; pos < list.size(); pos++) {
					otherCycles.add((JavaClassUnit) list.get(pos));
					otherCycleComponents.add(((JavaClassUnit) list.get(pos)).getComponent());
				}
				if (otherCycleComponents.size() > 1) {
					for (JavaClassUnit unit : otherCycles) {
						unit.setCycles(otherCycles);
					}
				}

				knowledge.put(this, LocalCycle);
				return LocalCycle;// 存在局部循环依赖
			}
		}

		list.add(this);// 将当前分析单元入栈

		if (this.getCeList().contains(list.get(0))) {// 直接依赖进行广度搜索
			for (JDependUnit unit : list) {
				if (!this.component.containsClass((JavaClassUnit) unit)) {
					return Cycle;// 存在循环依赖
				}
			}
		}

		L: for (JavaClassUnit efferent : this.getCeList()) {
			Integer rtnInteger = (Integer) knowledge.get(efferent);// 获取历史扫描数据
			if (rtnInteger == null) {// 没有扫描过的区域进行深度扫描
				int rtn = efferent.collectCycle(list, knowledge);// 深度搜索该区域
				if (rtn == Cycle) {// 存在循环依赖
					// 通知其他组件存在循环依赖
					for (int index = 1; index < list.size(); index++) {
						((JavaClassUnit) list.get(index)).setCycles(list);
					}
					return Cycle;
				} else if (rtn == StopCheckCycle) {
					break L;// 搜索深度大于20时停止
				}
			}
		}

		list.remove(this);// 将当前分析单元出栈

		knowledge.put(this, NoCycle);// 记录该对象扫描过的结果

		return NoCycle;// 不存在循环依赖
	}

	public JavaClassUnit clone() {

		JavaClassUnit obj = new JavaClassUnit(this.javaClass.clone());

		obj.abstractClassQualification = this.abstractClassQualification;
		obj.stable = this.stable;

		obj.setType(this.getType());

		return obj;
	}

	@Override
	public int compareTo(JDependUnit o) {
		return this.javaClass.compareTo(((JavaClassUnit) o).javaClass);
	}

	@Override
	public int hashCode() {
		return this.javaClass.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JavaClassUnit) {
			return javaClass.equals(((JavaClassUnit) obj).javaClass);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return this.javaClass.toString();
	}

	@Override
	public void clear() {
		super.clear();
		this.javaClass.clear();

		caList = null;
		ceList = null;
		relationList = null;
		afferents = null;
		efferents = null;
	}

	@Override
	public Object getValue(String metrics) {

		switch (metrics) {

		case JavaClassUnit.Place:
			return this.javaClass.getPlace();

		case JavaClassUnit.State:
			if (this.javaClass.isState()) {
				return MetricsMgr.HaveState;
			} else {
				return MetricsMgr.NoValue;
			}

		case JavaClassUnit.Stable:
			if (this.isStable()) {
				return MetricsMgr.Stability;
			} else {
				return MetricsMgr.NoValue;
			}

		case JavaClassUnit.isPrivateElement:
			if (!this.isUsedByExternal()) {
				return MetricsMgr.Private;
			} else {
				return MetricsMgr.NoValue;
			}

		case MetricsMgr.Ca:
			return this.getAfferentCoupling() + "|" + this.javaClass.getCaList().size();

		case MetricsMgr.Ce:
			return this.getEfferentCoupling() + "|" + this.javaClass.getCeList().size();

		case JavaClassUnit.ClassType:
			return this.javaClass.getClassType();

		case MetricsMgr.Cycle:
			if (this.getResult().getRunningContext().isCalJavaClassCycle()) {
				if (this.getContainsCycle()) {
					return MetricsMgr.Cyclic;
				} else {
					return MetricsMgr.NoValue;
				}
			} else {
				return MetricsMgr.NoValue;
			}

		default:
			return super.getValue(metrics);
		}

	}
}
