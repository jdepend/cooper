package jdepend.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.metadata.JavaPackage;
import jdepend.model.component.JavaPackageComponent;
import jdepend.model.component.PackageSubJDependUnit;
import jdepend.model.component.VirtualComponent;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.ComponentPathSegment;
import jdepend.model.util.JavaClassUnitUtil;
import jdepend.model.util.RelationCreator;

/**
 * 组件抽象类
 * 
 * 该抽象类主要代表可以进行指标定义的Component
 * 
 * 它也承担将JavaPackage集合聚集成Component的职责
 * 
 * @author <b>Abner</b>
 * 
 */
public abstract class Component extends AbstractSubJDependUnit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3010412682448187877L;

	private String title;

	private int layer;

	protected List<JavaClassUnit> javaClasses = new ArrayList<JavaClassUnit>();

	private transient AnalysisResult result;

	private transient AreaComponent areaComponent;// 所属组件区域，由识别设计动机模块计算得到

	private transient String steadyType;// 稳定性分类，由识别设计动机模块计算得到

	protected transient Map<String, JavaClassUnit> javaClassesForId = new HashMap<String, JavaClassUnit>();// 缓存

	protected transient Collection<Component> afferents = null;// 缓存

	protected transient Collection<Component> efferents = null;// 缓存

	private transient Collection<JavaPackage> javaPackages = null;// 缓存

	private transient String path = null;// 缓存

	private transient Float cohesion = null;
	private transient Float caCoupling = null;
	private transient Float ceCoupling = null;

	private transient Collection<Relation> relations = new ArrayList<Relation>();

	private transient Collection<? extends SubJDependUnit> subJDependUnits = null;// 缓存

	public static final int UndefinedComponentLevel = 0;

	public static final int PlatformComponentLevel = 1;

	public static final String PlatformComponentLevelDesc = "平台组件";

	public static final int DomainComponentLevel = 2;

	public static final String DomainComponentLevelDesc = "领域业务组件";

	public static final int AppComponentLevel = 3;

	public static final String AppComponentLevelDesc = "应用业务组件";

	public static final int InteractiveComponentLevel = 4;

	public static final String InteractiveComponentLevelDesc = "交互组件";

	public static final String MutabilityType = "MutabilityType";

	public static final String MiddleType = "MiddleType";

	public static final String StableType = "StableType";

	private static final float SDPDifference = 0.1F;

	public static final String Area = "Area";

	public Component() {
	}

	public Component(String name) {
		super(name);
	}

	/**
	 * 初始化
	 * 
	 * 该方法将在客户端初始化Command时调用执行
	 * 
	 * @param group
	 * @param command
	 * @param info
	 * @throws JDependException
	 */
	public void init(String group, String command, String info) throws JDependException {
	}

	/**
	 * 得到Component列表
	 * 
	 * 在联机模式下，该方法将在服务器端执行
	 * 
	 * @param javaPackages
	 * @return
	 * @throws JDependException
	 */
	public final List<Component> list(Collection<JavaPackage> javaPackages) throws JDependException {
		List<Component> components = this.doList(javaPackages);
		this.filterExternalJavaClass(components);
		return components;
	}

	/**
	 * 过滤掉不被组件包含的JavaClass
	 * 
	 * @param components
	 */
	protected void filterExternalJavaClass(Collection<Component> components) {
		Collection<JavaClass> javaClasses = JavaClassUnitUtil.getAllClasses(components);
		for (JavaClass javaClass : javaClasses) {
			javaClass.filterExternalJavaClass(javaClasses);
		}
	}

	/**
	 * 每个Component需要装载上包含的javaPackages。
	 * 
	 * @param javaPackages
	 * @return
	 * @throws JDependException
	 */
	protected abstract List<Component> doList(Collection<JavaPackage> javaPackages) throws JDependException;

	@Override
	public boolean isInner() {
		for (JavaClassUnit javaClass : this.getClasses()) {
			if (javaClass.isInner()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Collection<JavaClassUnit> getClasses() {
		return this.javaClasses;
	}

	public JavaClassUnit getTheClass(String classId) {
		return this.javaClassesForId.get(classId);
	}

	@Override
	public synchronized Collection<JavaPackage> getJavaPackages() {
		if (this.javaPackages == null) {
			this.javaPackages = new HashSet<JavaPackage>();
			for (JavaClassUnit javaClass : this.javaClasses) {
				this.javaPackages.add(javaClass.getJavaClass().getJavaPackage());
			}
		}
		return this.javaPackages;
	}

	@Override
	public synchronized String getPath() {
		if (path == null) {
			// 获得指定单元的包列表
			List<String> javaPackageNames = new ArrayList<String>();
			for (JavaPackage javaPackage : this.getJavaPackages()) {
				javaPackageNames.add(javaPackage.getName());
			}
			path = Component.getDefaultComponentName(javaPackageNames, true);
		}
		return path;
	}

	/**
	 * 建立组件与类之间的双向关联
	 * 
	 * @param javaClass
	 */
	public synchronized void addJavaClass(JavaClassUnit javaClass) {
		if (!this.javaClasses.contains(javaClass)) {
			javaClass.setComponent(this);
			this.javaClasses.add(javaClass);
			this.javaClassesForId.put(javaClass.getId(), javaClass);
		}
	}

	public synchronized boolean removeJavaClass(JavaClassUnit javaClass) {
		if (this.javaClasses.remove(javaClass)) {
			if (javaClass.getComponent().equals(this)) {
				javaClass.setComponent(null);
			}
			this.javaClassesForId.remove(javaClass.getId());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean containsClass(JavaClassUnit javaClass) {
		return this.containsClass(javaClass.getJavaClass());
	}

	public boolean containsClass(JavaClass javaClass) {
		// javaClass和JavaClassUnit共用一个Id
		if (javaClass.isInnerClass()) {
			JavaClass hostClass = javaClass.getHostClass();
			if (hostClass != null) {
				return this.javaClassesForId.containsKey(hostClass.getId());
			} else {
				return this.javaClassesForId.containsKey(javaClass.getId());
			}
		} else {
			return this.javaClassesForId.containsKey(javaClass.getId());
		}
	}

	@Override
	public int getClassCount() {
		return this.getClasses().size();
	}

	@Override
	public int getAbstractClassCount() {

		int rtn = 0;

		for (JavaClassUnit javaClass : this.getClasses()) {
			rtn += javaClass.getAbstractClassCount();
		}

		return rtn;
	}

	@Override
	public int getConcreteClassCount() {
		return this.getClassCount() - this.getAbstractClassCount();
	}

	@Override
	public synchronized Collection<Component> getAfferents() {

		if (this.afferents == null) {
			this.afferents = new HashSet<Component>();
			for (Relation relation : relations) {
				if (relation.getDepend().getComponent().equals(this)) {
					this.afferents.add(relation.getCurrent().getComponent());
				}
			}
		}
		return this.afferents;
	}

	@Override
	public synchronized Collection<Component> getEfferents() {

		if (this.efferents == null) {
			this.efferents = new HashSet<Component>();
			for (Relation relation : relations) {
				if (relation.getCurrent().getComponent().equals(this)) {
					this.efferents.add(relation.getDepend().getComponent());
				}
			}
		}
		return this.efferents;
	}

	public boolean isRelation(Component component) {
		return this.getEfferents().contains(component) || this.getAfferents().contains(component);
	}

	@Override
	public RelationDetail ceCouplingDetail(JDependUnit dependUnit) {

		RelationDetail detail = new RelationDetail();
		if (this.equals(dependUnit)) {
			return detail;
		}

		for (Relation relation : this.relations) {
			if (relation.getDepend().getComponent().equals(dependUnit)) {
				return relation.getDetail();
			}
		}

		float intensity = 0;
		for (JavaClassUnit javaClass : this.getClasses()) {
			RelationDetail relationDetail = javaClass.ceCouplingDetail(dependUnit);
			intensity += relationDetail.getIntensity();
			detail.addItems(relationDetail.getItems());
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

		for (Relation relation : this.relations) {
			if (relation.getCurrent().getComponent().equals(dependUnit)) {
				return relation.getDetail();
			}
		}

		float intensity = 0;
		for (JavaClassUnit javaClass : this.getClasses()) {
			RelationDetail relationDetail = javaClass.caCouplingDetail(dependUnit);
			intensity += relationDetail.getIntensity();
			detail.addItems(relationDetail.getItems());
		}
		detail.setIntensity(intensity);

		return detail;
	}

	/**
	 * 计算与特定组件的传出耦合细节信息
	 * 
	 * @param jDependUnit
	 * @return
	 */
	public RelationDetail calCeCouplingDetail(Component component) {

		RelationDetail detail = new RelationDetail();
		if (this.equals(component)) {
			return detail;
		}

		float intensity = 0;
		for (JavaClassUnit javaClass : this.getClasses()) {
			for (JavaClassRelationItem relationItem : javaClass.getJavaClass().getCeItems()) {
				if (component.containsClass(relationItem.getTarget())) {
					detail.addItem(relationItem);
					intensity += relationItem.getRelationIntensity();
				}
			}
		}

		detail.setIntensity(intensity);

		return detail;
	}

	public Relation getCeTheRelation(Component component) {
		for (Relation relation : this.relations) {
			if (relation.getDepend().getComponent().equals(component)) {
				return relation;
			}
		}
		return null;
	}

	public Relation getCaTheRelation(Component component) {
		for (Relation relation : this.relations) {
			if (relation.getCurrent().getComponent().equals(component)) {
				return relation;
			}
		}
		return null;
	}

	public Collection<Component> getRelationComponents() {
		Collection<Component> relationComponents = new HashSet<Component>();
		for (Relation relation : this.relations) {
			if (relation.getCurrent().getComponent().equals(this)) {
				relationComponents.add(relation.getDepend().getComponent());
			} else {
				relationComponents.add(relation.getCurrent().getComponent());
			}
		}
		return relationComponents;
	}

	@Override
	public synchronized float caCoupling() {
		if (caCoupling == null) {
			float intensity = 0;
			for (Relation relation : this.relations) {
				if (relation.getDepend().getComponent().equals(this)) {
					intensity += relation.getIntensity();
				}
			}
			this.caCoupling = intensity;
		}
		return this.caCoupling;
	}

	@Override
	public synchronized float ceCoupling() {
		if (ceCoupling == null) {
			float intensity = 0;
			for (Relation relation : this.relations) {
				if (relation.getCurrent().getComponent().equals(this)) {
					intensity += relation.getIntensity();
				}
			}
			this.ceCoupling = intensity;
		}
		return this.ceCoupling;
	}

	public synchronized void addRelation(Relation relation) {
		if (!this.relations.contains(relation)) {
			this.relations.add(relation);
		}
	}

	public Collection<Relation> getRelations() {
		return this.relations;
	}

	public Collection<Relation> getAfferentRelations() {
		Collection<Relation> relations = new HashSet<Relation>();
		for (Relation relation : this.relations) {
			if (relation.getDepend().getComponent().equals(this)) {
				relations.add(relation);
			}
		}
		return relations;
	}

	public Collection<Relation> getEfferentRelations() {
		Collection<Relation> relations = new HashSet<Relation>();
		for (Relation relation : this.relations) {
			if (relation.getCurrent().getComponent().equals(this)) {
				relations.add(relation);
			}
		}
		return relations;
	}

	public boolean stability(Component component) {
		return this.getStability() + SDPDifference < component.getStability();
	}

	public Collection<Relation> open() {

		Collection<Relation> allRelations = new HashSet<Relation>();

		Collection<Component> innerComponents = new HashSet<Component>();
		Component virtualComponent;
		for (JavaClassUnit javaClass : this.getClasses()) {
			virtualComponent = new VirtualComponent(javaClass);
			innerComponents.add(virtualComponent);
		}

		Collection<Relation> innerRelations = new RelationCreator().create(innerComponents);
		allRelations.addAll(innerRelations);

		Collection<Component> outerComponents = this.getRelationComponents();

		Collection<Relation> ceRelations = new RelationCreator(true, false).create(innerComponents, outerComponents);
		allRelations.addAll(ceRelations);

		Collection<Relation> caRelations = new RelationCreator(false, true).create(outerComponents, innerComponents);
		allRelations.addAll(caRelations);

		return allRelations;
	}

	@Override
	protected GroupInfoCalculator createGroupInfoCalculator() {
		throw new RuntimeException("子类需要复写该方法");
	}

	@Override
	public void clear() {
		super.clear();

		this.afferents = null;
		this.efferents = null;
		this.javaPackages = null;
		this.path = null;

		this.cohesion = null;
		this.caCoupling = null;
		this.ceCoupling = null;
		this.relations = new ArrayList<Relation>();

		this.subJDependUnits = null;

		this.areaComponent = null;
		this.steadyType = null;
	}

	@Override
	public synchronized float getCohesion() {
		if (cohesion == null) {
			float intensity = 0;
			for (JavaClassUnit javaClass : this.getClasses()) {
				intensity += javaClass.getCohesion();
			}
			cohesion = intensity / 2;
		}
		return cohesion;
	}

	@Override
	public Float getBalance() {
		if (this.getCoupling() == 0) {
			return null;
		}
		Collection<? extends SubJDependUnit> subJDependUnits = this.getSubJDependUnits();
		if (subJDependUnits.size() > 0) {
			if (subJDependUnits.size() == 1) {
				return 1F;
			} else {
				float balance = 0F;
				for (SubJDependUnit subJDependUnit : subJDependUnits) {
					balance += subJDependUnit.getBalance();
				}
				return balance / subJDependUnits.size();
			}
		} else {
			return 0F;
		}
	}

	/**
	 * 获取用于计算内聚性的子元素集合
	 * 
	 * @return
	 */
	public Collection<? extends SubJDependUnit> getSubJDependUnits() {
		if (this.subJDependUnits == null) {
			if (this.getJavaPackages().size() == 0 || this.getJavaPackages().size() == 1) {
				this.subJDependUnits = this.javaClasses;
			} else {
				Collection<PackageSubJDependUnit> packageComponents = new HashSet<PackageSubJDependUnit>();
				for (JavaPackage javaPackage : this.getJavaPackages()) {
					packageComponents.add(new PackageSubJDependUnit(javaPackage, this.getResult()));
				}
				Collection<Component> outerComponents = this.getRelationComponents();
				new RelationCreator().create(packageComponents);
				new RelationCreator(false, true).create(outerComponents, packageComponents);
				new RelationCreator(true, false).create(packageComponents, outerComponents);

				this.subJDependUnits = packageComponents;
			}
		}
		return this.subJDependUnits;
	}

	public SubJDependUnit getTheSubJDependUnit(String name) {
		for (SubJDependUnit subJDependUnit : this.getSubJDependUnits()) {
			if (subJDependUnit.getName().equals(name)) {
				return subJDependUnit;
			}
		}
		return null;
	}

	public Component clone(Map<String, JavaClassUnit> javaClasses) throws JDependException {
		try {
			Component obj = this.getClass().newInstance();
			obj.setName(this.getName());
			obj.setTitle(this.getTitle());
			obj.setType(this.getType());
			obj.setLayer(this.getLayer());
			obj.setAreaComponent(this.getAreaComponent());

			for (JavaClassUnit javaClass : this.javaClasses) {
				obj.addJavaClass(javaClasses.get(javaClass.getId()));
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JDependException("克隆" + this.getName() + "出错", e);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public boolean isDefinedComponentLevel() {
		return this.layer != UndefinedComponentLevel;
	}

	public String getLayerDesc() {
		return layerDesc(this.layer);
	}

	public static String layerDesc(int layer) {
		if (layer == jdepend.model.Component.UndefinedComponentLevel) {
			return null;
		} else if (layer == DomainComponentLevel) {
			return DomainComponentLevelDesc;
		} else if (layer == AppComponentLevel) {
			return AppComponentLevelDesc;
		} else if (layer == PlatformComponentLevel) {
			return PlatformComponentLevelDesc;
		} else if (layer == InteractiveComponentLevel) {
			return InteractiveComponentLevelDesc;
		} else {
			return null;
		}
	}

	public AreaComponent getAreaComponent() {
		return areaComponent;
	}

	public void setAreaComponent(AreaComponent areaComponent) {
		this.areaComponent = areaComponent;
	}

	public String getSteadyType() {
		return steadyType;
	}

	public void setSteadyType(String steadyType) {
		this.steadyType = steadyType;
	}

	public void setResult(AnalysisResult result) {
		this.result = result;
	}

	@Override
	public AnalysisResult getResult() {
		return this.result;
	}

	public static Component getDefaultComponent() {
		return new JavaPackageComponent();
	}

	@Override
	public int collectCycle(List<JDependUnit> list, Map<JDependUnit, Integer> knowledge) {

		if (list.size() > 20) {
			LogUtil.getInstance(JavaClassUnit.class).systemWarning(
					"Component[" + list.get(0).getName() + "][" + this.getName() + "] collectCycle 搜索深度大于20停止搜索");
			return StopCheckCycle;// 搜索深度大于20时停止
		}

		if (list.contains(this)) {
			if (list.get(0).equals(this)) {
				return Cycle;// 存在循环依赖
			} else {
				// 通知其他组件存在循环依赖
				int index;
				L: for (index = 1; index < list.size(); index++) {
					if (list.get(index).equals(this)) {
						break L;
					}
				}
				List<Component> otherCycles = new ArrayList<Component>();
				for (int pos = index; pos < list.size(); pos++) {
					otherCycles.add((Component) list.get(pos));
				}
				for (Component unit : otherCycles) {
					unit.setCycles(otherCycles);
				}

				knowledge.put(this, LocalCycle);
				return LocalCycle;// 存在局部循环依赖
			}
		}

		list.add(this);// 将当前分析单元入栈

		if (this.getEfferents().contains(list.get(0))) {// 直接依赖进行广度搜索
			return Cycle;
		}

		L: for (Component efferent : this.getEfferents()) {
			Integer rtnInteger = (Integer) knowledge.get(efferent);// 获取历史扫描数据
			if (rtnInteger == null) {// 没有扫描过的区域进行深度扫描
				int rtn = efferent.collectCycle(list, knowledge);// 深度搜索该区域
				if (rtn == Cycle) {// 存在循环依赖
					// 通知其他组件存在循环依赖
					for (int index = 1; index < list.size(); index++) {
						((Component) list.get(index)).setCycles(list);
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

	@Override
	public Object getValue(String metrics) {
		if (metrics.equals(Area)) {
			return this.getAreaComponent();
		} else {
			return super.getValue(metrics);
		}
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		this.relations = new ArrayList<Relation>();

		this.javaClassesForId = new HashMap<String, JavaClassUnit>();
		for (JavaClassUnit javaClass : this.javaClasses) {
			this.javaClassesForId.put(javaClass.getId(), javaClass);
		}
	}

	@Override
	public String toString() {

		return "名称:" + this.getName() + "标题:" + this.getTitle() + " 是否内部:" + this.isInner() + " 类总数:"
				+ this.getClassCount() + " 具体类:" + this.getConcreteClassCount() + " 抽象类:"
				+ this.getAbstractClassCount() + " 总行数:" + this.getLineCount() + " 传入:" + this.getAfferentCoupling()
				+ " 传出:" + this.getEfferentCoupling() + " 抽象程度:"
				+ MetricsFormat.toFormattedMetrics(this.getAbstractness()) + " 稳定性:"
				+ MetricsFormat.toFormattedMetrics(this.getStability()) + " 合理性:"
				+ MetricsFormat.toFormattedMetrics(this.getDistance()) + " 耦合值:"
				+ MetricsFormat.toFormattedMetrics(this.getCoupling()) + " 内聚值:"
				+ MetricsFormat.toFormattedMetrics(this.getCohesion()) + " 内聚性:"
				+ MetricsFormat.toFormattedMetrics(this.getBalance()) + " 封装性:"
				+ (this.getEncapsulation() == null ? "-" : MetricsFormat.toFormattedMetrics(this.getEncapsulation()));
	}

	public static String getDefaultComponentName(List<String> javaPackages, boolean isFullComponentName) {
		if (javaPackages.size() == 1) {
			if (isFullComponentName) {
				return javaPackages.get(0);
			} else {
				return javaPackages.get(0).substring(javaPackages.get(0).lastIndexOf(".") + 1);
			}
		} else {
			List<ComponentPathSegment> segments = ComponentPathSegment.create(javaPackages);

			String oldSegment = "";
			String oldPath = "";
			L: for (ComponentPathSegment segment : segments) {
				if (segment.getCount() < javaPackages.size()) {
					break L;
				}
				oldSegment = segment.getName();
				oldPath = oldPath + "." + segment.getName();
			}

			if (isFullComponentName) {
				if (oldPath.length() == 0) {
					return oldPath;
				} else {
					return oldPath.substring(1);
				}
			} else {
				return oldSegment;
			}
		}
	}
}
