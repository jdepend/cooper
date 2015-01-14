package jdepend.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.util.MetricsFormat;
import jdepend.model.component.VirtualComponent;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.ComponentPathSegment;
import jdepend.model.util.JavaClassUtil;

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
public abstract class Component extends AbstractJDependUnit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3010412682448187877L;

	private String title;

	private int layer;

	private List<JavaClass> javaClasses = new ArrayList<JavaClass>();

	private transient AnalysisResult result;

	private transient AreaComponent areaComponent;// 所属组件区域，由识别设计动机模块计算得到

	private transient String steadyType;// 稳定性分类，由识别设计动机模块计算得到

	private transient Map<String, JavaClass> javaClassesForId = new HashMap<String, JavaClass>();// 缓存

	protected transient Collection<Component> afferents = null;// 缓存

	protected transient Collection<Component> efferents = null;// 缓存

	private transient Collection<JavaPackage> javaPackages = null;// 缓存

	private transient String path = null;// 缓存

	private transient Float caCoupling = null;
	private transient Float ceCoupling = null;

	private transient Collection<Relation> relations = new ArrayList<Relation>();

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
		this.filterExternalJavaClassRelationItems(components);
		return components;
	}

	/**
	 * 过滤掉不被组件包含的JavaClassRelationItems
	 * 
	 * @param components
	 */
	protected void filterExternalJavaClassRelationItems(Collection<Component> components) {
		Collection<JavaClass> javaClasses = JavaClassUtil.getClasses(components);
		Iterator<JavaClassRelationItem> it;
		for (JavaClass javaClass : javaClasses) {
			it = javaClass.getCaItems().iterator();
			while (it.hasNext()) {
				if (!javaClasses.contains(it.next().getDepend())) {
					it.remove();
				}
			}
			it = javaClass.getCeItems().iterator();
			while (it.hasNext()) {
				if (!javaClasses.contains(it.next().getDepend())) {
					it.remove();
				}
			}
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
		for (JavaClass javaClass : this.getClasses()) {
			if (javaClass.isInner()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Collection<JavaClass> getClasses() {
		return this.javaClasses;
	}

	public JavaClass getTheClass(String current) {
		return this.javaClassesForId.get(current);
	}

	@Override
	public synchronized Collection<JavaPackage> getJavaPackages() {
		if (this.javaPackages == null) {
			this.javaPackages = new ArrayList<JavaPackage>();
			for (JavaClass javaClass : this.javaClasses) {
				if (!this.javaPackages.contains(javaClass.getJavaPackage())) {
					this.javaPackages.add(javaClass.getJavaPackage());
				}
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

	@Override
	public String getArea() {
		if (this.areaComponent == null) {
			return null;
		} else {
			return this.areaComponent.getName();
		}
	}

	/**
	 * 建立组件与类之间的双向关联
	 * 
	 * @param javaClass
	 */
	public synchronized void addJavaClass(JavaClass javaClass) {
		if (!this.javaClasses.contains(javaClass)) {
			javaClass.setComponent(this);
			this.javaClasses.add(javaClass);
			this.javaClassesForId.put(javaClass.getId(), javaClass);
		}
	}

	/**
	 * 建立组件与类之间的单向关联
	 * 
	 * @param javaClass
	 */
	public synchronized void joinJavaClass(JavaClass javaClass) {
		if (!this.javaClasses.contains(javaClass)) {
			this.javaClasses.add(javaClass);
			this.javaClassesForId.put(javaClass.getId(), javaClass);
		}
	}

	public synchronized boolean removeJavaClass(JavaClass javaClass) {
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
	public boolean containsClass(JavaClass javaClass) {
		return this.javaClassesForId.containsKey(javaClass.getId());
	}

	@Override
	public int getClassCount() {
		return this.getClasses().size();
	}

	@Override
	public int getAbstractClassCount() {

		int rtn = 0;

		for (JavaClass javaClass : this.getClasses()) {
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
		return detail;
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

	public boolean stability(Component component) {
		return this.getStability() + SDPDifference < component.getStability();
	}

	public Collection<Relation> open() {
		List<Component> components = new ArrayList<Component>();
		for (Element element : Relation.calElements(relations)) {
			components.add(element.getComponent());
		}
		Collection<Component> allComponents = new ArrayList<Component>();
		Collection<Component> innerComponents = new HashSet<Component>();
		Component virtualComponent;
		for (Component component : components) {
			if (component.equals(this)) {
				for (JavaClass javaClass : this.getClasses()) {
					virtualComponent = new VirtualComponent(javaClass);
					allComponents.add(virtualComponent);
					innerComponents.add(virtualComponent);
				}
			} else {
				allComponents.add(new VirtualComponent(component));
			}
		}

		Collection<Relation> relations = new ArrayList<Relation>();
		Relation r;
		RelationDetail detail;
		Map<String, Element> elements = new HashMap<String, Element>();
		for (Component left : allComponents) {
			for (Component right : allComponents) {
				if (innerComponents.contains(left) || innerComponents.contains(right)) {
					detail = left.calCeCouplingDetail(right);
					if (detail.getIntensity() != 0) {
						r = new Relation();
						r.setCurrent(this.createElement(left, elements));
						r.setDepend(this.createElement(right, elements));
						r.setDetail(detail);
						relations.add(r);
					}
				}
			}
		}

		return relations;
	}

	private Element createElement(Component unit, Map<String, Element> elements) {
		Element element = elements.get(unit.getName());
		if (element == null) {
			element = new Element(unit);
			elements.put(unit.getName(), element);
		}
		return element;
	}

	@Override
	public float getObjectOriented() {
		if (this.getClassCount() == 0) {
			return 0.0F;
		}
		Float oo = 0.0F;
		for (JavaClass javaClass : this.javaClasses) {
			oo += javaClass.getObjectOriented();
		}
		return oo / this.getClassCount();
	}

	@Override
	public void clear() {
		super.clear();

		this.afferents = null;
		this.efferents = null;
		this.javaPackages = null;
		this.path = null;

		this.caCoupling = null;
		this.ceCoupling = null;
		this.relations = new ArrayList<Relation>();

		this.areaComponent = null;
		this.steadyType = null;
	}

	@Override
	public float getBalance() {
		if (this.javaClasses.size() > 0) {
			if (this.javaClasses.size() == 1) {
				return 1F;
			} else {
				float balance = 0F;
				for (JavaClass javaClass : this.javaClasses) {
					balance += javaClass.getBalance();
				}
				return balance / this.javaClasses.size();
			}
		} else {
			return 0F;
		}
	}

	public Component clone(Map<String, JavaClass> javaClasses) throws JDependException {
		try {
			Component obj = this.getClass().newInstance();
			obj.setName(this.getName());
			obj.setTitle(this.getTitle());
			obj.setType(this.getType());
			obj.setLayer(this.getLayer());
			obj.setAreaComponent(this.getAreaComponent());

			for (JavaClass javaClass : this.javaClasses) {
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

	@Override
	public int collectCycle(List<JDependUnit> list, Map<JDependUnit, Integer> knowledge) {

		if (list.size() > 20) {
			LogUtil.getInstance(JavaClass.class).systemWarning(
					"Component[" + this.getName() + "] collectCycle 搜索深度大于20停止搜索");
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

		for (Component efferent : this.getEfferents()) {
			Integer rtnInteger = (Integer) knowledge.get(efferent);// 获取历史扫描数据
			if (rtnInteger == null) {// 没有扫描过的区域进行深度扫描
				int rtn = efferent.collectCycle(list, knowledge);// 深度搜索该区域
				if (rtn == Cycle) {// 存在循环依赖
					// 通知其他组件存在循环依赖
					for (int index = 1; index < list.size(); index++) {
						((Component) list.get(index)).setCycles(list);
					}
					return Cycle;
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

		this.javaClassesForId = new HashMap<String, JavaClass>();
		for (JavaClass javaClass : this.javaClasses) {
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
				+ (this.getEncapsulation() == null ? "-" : MetricsFormat.toFormattedMetrics(this.getEncapsulation()))
				+ " 面向对象:" + MetricsFormat.toFormattedMetrics(this.getObjectOriented());
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
