package jdepend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.context.JDependContext;
import jdepend.framework.context.Scope.SCOPE;
import jdepend.framework.exception.JDependException;
import jdepend.framework.util.ParseUtil;
import jdepend.model.relationtype.FieldRelation;
import jdepend.model.relationtype.JavaClassRelationTypeMgr;

import org.apache.bcel.Constants;

/**
 * The <code>JavaClass</code> class represents a Java class or interface.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class JavaClass extends AbstractJDependUnit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3619193239851101008L;

	private int access_flags;
	private String packageName;
	private Boolean abstractClassQualification;// 是否具有抽象类资格
	private Collection<String> imports;
	private int lineCount;
	private boolean isInner;
	private boolean stable = false;

	private JavaPackage javaPackage;
	private Component component;

	private int haveState = UnCalculate;// 缓存是否存在状态

	private JavaClassDetail detail;

	private transient Collection<Method> methods;

	private transient Map<Method, Method> overrideMethods;

	private transient Collection<JavaClass> supers;

	private transient Collection<JavaClass> interfaces;

	private transient Collection<JavaClass> subClasses;

	private Collection<JavaClassRelationItem> caItems = new ArrayList<JavaClassRelationItem>();

	private Collection<JavaClassRelationItem> ceItems = new ArrayList<JavaClassRelationItem>();

	private boolean isIncludeTransactionalAnnotation;

	public final static int Cycle = 2;
	public final static int LocalCycle = 1;
	public final static int NoCycle = 0;

	private final static int UnCalculate = -2;
	private final static int HaveState = 1;
	private final static int NoHaveState = 0;
	private final static int Searched = -1;

	private final static String haveStateJavaClasses = "haveStateJavaClasses";

	private final static List<String> baseTypes = new ArrayList<String>(8);

	static {
		baseTypes.add("boolean");
		baseTypes.add("byte");
		baseTypes.add("char");
		baseTypes.add("double");
		baseTypes.add("float");
		baseTypes.add("int");
		baseTypes.add("long");
		baseTypes.add("short");
	}

	private transient Collection<JavaClass> caList = null;
	private transient Collection<JavaClass> ceList = null;
	private transient Collection<JavaClass> relationList = null;
	private transient Collection<JavaClass> afferents = null;
	private transient Collection<JavaClass> efferents = null;
	private transient Float cohesion = null;
	private transient Float balance = null;
	private transient Float objectOriented = null;
	private transient GroupCouplingInfo groupCouplingInfo = null;

	private transient Collection<JavaClass> invokeClasses;

	public JavaClass(String name, boolean isInner, int access_flags) {
		this(name, isInner);
		this.access_flags = access_flags;
	}

	public JavaClass(String name, boolean isInner) {
		this.setName(name);
		this.packageName = JavaPackage.Default;
		this.imports = new HashSet<String>();
		this.detail = new JavaClassDetail(this);
		this.isInner = isInner;
		this.isIncludeTransactionalAnnotation = false;
	}

	/**
	 * 是否是内部类
	 * 
	 * @return
	 */
	public boolean isInnerClass() {
		if (this.getName().toLowerCase().indexOf("$") > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void setPackageName(String name) {
		packageName = name;
	}

	public String getPackageName() {
		return packageName;
	}

	@Override
	public String getPath() {
		return packageName;
	}

	public Collection<String> getImportedPackages() {
		return imports;
	}

	/**
	 * 是否是分析目标中包含的Class
	 */
	public boolean isInner() {
		return this.isInner;
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
		for (JavaClass relationClass : this.getRelationList()) {
			if (this.getComponent().containsClass(relationClass)) {
				return false;
			}
		}
		return true;
	}

	public JavaClassDetail getDetail() {
		return detail;
	}

	public void setDetail(JavaClassDetail detail) {
		this.detail = detail;
	}

	public Collection<Attribute> getAttributes() {
		return this.detail.getAttributes();
	}

	/**
	 * 得到其他类可以使用的方法列表（不包含继承父类的方法）
	 * 
	 * @return
	 */
	public Collection<Method> getSelfMethods() {
		return this.detail.getMethods();
	}

	/**
	 * 得到覆盖了父类方法的方法列表
	 * 
	 * @return
	 */
	public Map<Method, Method> getOverrideMethods() {
		if (this.overrideMethods == null) {
			this.overrideMethods = new LinkedHashMap<Method, Method>();
			for (JavaClass superClass : this.getSupers()) {
				for (Method method : superClass.getSelfMethods()) {
					if (!method.isConstruction()) {
						for (Method selfMethod : this.getSelfMethods()) {
							if (selfMethod.isOverride(method)) {
								this.overrideMethods.put(selfMethod, method);
							}
						}
					}
				}
			}
		}
		return this.overrideMethods;
	}

	/**
	 * 得到其他类可以使用的方法列表（包含继承父类的方法）
	 * 
	 * @return
	 */
	public Collection<Method> getMethods() {
		if (this.methods == null) {
			boolean isOverride;
			this.methods = new ArrayList<Method>();
			for (JavaClass superClass : this.getSupers()) {
				for (Method method : superClass.getSelfMethods()) {
					if (!method.isConstruction() && (method.isPublic() || method.isProtected())) {
						isOverride = false;
						L: for (Method selfMethod : this.getSelfMethods()) {
							if (selfMethod.isOverride(method)) {
								isOverride = true;
								break L;
							}
						}
						if (!isOverride) {
							this.methods.add(method);
						}
					}
				}
			}
			this.methods.addAll(this.getSelfMethods());
		}
		return this.methods;
	}

	public void addSelfMethod(Method method) {
		this.detail.addMethod(method);
	}

	public Collection<TableInfo> getTables() {
		return this.detail.getTables();
	}

	/**
	 * 得到所有的父类和接口列表
	 * 
	 * @return
	 */
	public Collection<JavaClass> getSupers() {
		if (this.supers == null) {
			this.supers = new HashSet<JavaClass>();
			JavaClass superClass = this.getSuperClass();
			if (superClass != null) {
				supers.add(superClass);
				supers.addAll(superClass.getSupers());
			}
			supers.addAll(this.getAllInterfaces());
		}
		return this.supers;
	}

	private Collection<JavaClass> getAllInterfaces() {
		if (this.interfaces == null) {
			this.interfaces = new HashSet<JavaClass>();
			for (JavaClass interfaceClass : this.getInterfaces()) {
				this.interfaces.add(interfaceClass);
				Collection<JavaClass> interfaceClasses = interfaceClass.getInterfaces();
				for (JavaClass interfaceClass1 : interfaceClasses) {
					this.interfaces.add(interfaceClass1);
					this.interfaces.addAll(interfaceClass1.getAllInterfaces());
				}
			}
		}
		return this.interfaces;
	}

	private Collection<JavaClass> getInterfaces() {
		return this.detail.getInterfaces();
	}

	private JavaClass getSuperClass() {
		return this.detail.getSuperClass();
	}

	public Collection<JavaClass> getSubClasses() {
		if (this.subClasses == null) {
			this.subClasses = new HashSet<JavaClass>();
			for (JavaClassRelationItem item : this.getCaItems()) {
				if (item.getType().equals(JavaClassRelationTypeMgr.getInstance().getInheritRelation())) {
					this.subClasses.add(item.getDepend());
					this.subClasses.addAll(item.getDepend().getSubClasses());
				}
			}
		}
		return this.subClasses;
	}

	public Collection<JavaClassRelationItem> getCaItems() {
		return caItems;
	}

	public Collection<JavaClass> getCaList() {
		if (this.caList == null) {
			Collection<JavaClass> javaClasses = new HashSet<JavaClass>();
			for (JavaClassRelationItem item : getCaItems()) {
				if (!javaClasses.contains(item.getDepend())) {
					javaClasses.add(item.getDepend());
				}
			}
			this.caList = javaClasses;
		}
		return this.caList;
	}

	public Collection<JavaClass> getRelationList() {
		if (this.relationList == null) {
			Collection<JavaClass> javaClasses = this.getCaList();
			for (JavaClass javaClass : this.getCeList()) {
				if (!javaClasses.contains(javaClass)) {
					javaClasses.add(javaClass);
				}
			}
			this.relationList = javaClasses;
		}
		return this.relationList;
	}

	public void addCaItems(JavaClassRelationItem caItem) {
		if (!this.caItems.contains(caItem)) {
			this.caItems.add(caItem);
		}
	}

	public Collection<JavaClassRelationItem> getCeItems() {
		return ceItems;
	}

	public Collection<JavaClass> getCeList() {
		if (this.ceList == null) {
			Collection<JavaClass> javaClasses = new HashSet<JavaClass>();
			for (JavaClassRelationItem item : getCeItems()) {
				if (!javaClasses.contains(item.getDepend())) {
					javaClasses.add(item.getDepend());
				}
			}
			this.ceList = javaClasses;
		}
		return this.ceList;
	}

	public void addCeItems(JavaClassRelationItem ceItem) {
		if (!this.ceItems.contains(ceItem)) {
			this.ceItems.add(ceItem);
		}
	}

	public int getAbstractClassCount() {
		if (this.abstractClassQualification == null) {
			return this.isAbstract() ? 1 : 0;
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

	public float getAbstractness() {
		return this.getAbstractClassCount();
	}

	public int getLineCount() {
		if (this.isInnerClass()) {
			return 0;
		} else {
			return this.lineCount;
		}
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	@Override
	public Collection<JavaClass> getAfferents() {
		if (this.afferents == null) {
			Collection<JavaClass> afferents = new HashSet<JavaClass>();

			for (JavaClass javaClass : this.getCaList()) {
				if (!this.component.containsClass(javaClass) && !afferents.contains(javaClass)) {
					afferents.add(javaClass);
				}
			}
			this.afferents = afferents;
		}
		return this.afferents;
	}

	@Override
	public Collection<JavaClass> getEfferents() {
		if (this.efferents == null) {
			Collection<JavaClass> efferents = new HashSet<JavaClass>();

			for (JavaClass javaClass : this.getCeList()) {
				if (!this.component.containsClass(javaClass) && !efferents.contains(javaClass)) {
					efferents.add(javaClass);
				}
			}
			this.efferents = efferents;
		}
		return this.efferents;
	}

	@Override
	public float ceCoupling(JDependUnit jdependUnit) {

		if (this.equals(jdependUnit)) {
			return 0;
		}
		float intensity = 0;
		for (JavaClassRelationItem relationItem : this.ceItems) {
			if (jdependUnit.containsClass(relationItem.getDepend())) {
				intensity += relationItem.calRelationIntensity();
			}
		}
		return intensity;
	}

	@Override
	public float caCoupling(JDependUnit jdependUnit) {

		if (this.equals(jdependUnit)) {
			return 0;
		}
		float intensity = 0;
		for (JavaClassRelationItem relationItem : this.caItems) {
			if (jdependUnit.containsClass(relationItem.getDepend())) {
				intensity += relationItem.calRelationIntensity();
			}
		}
		return intensity;
	}

	@Override
	public Collection<JavaClassRelationItem> ceCouplingDetail(JDependUnit dependUnit) {

		Collection<JavaClassRelationItem> detail = new ArrayList<JavaClassRelationItem>();
		if (this.equals(dependUnit)) {
			return detail;
		}

		for (JavaClassRelationItem relationItem : this.ceItems) {
			if (dependUnit.containsClass(relationItem.getDepend())) {
				detail.add(relationItem);
			}
		}
		return detail;
	}

	@Override
	public Collection<JavaClassRelationItem> caCouplingDetail(JDependUnit dependUnit) {

		Collection<JavaClassRelationItem> detail = new ArrayList<JavaClassRelationItem>();
		if (this.equals(dependUnit)) {
			return detail;
		}

		for (JavaClassRelationItem relationItem : this.caItems) {
			if (dependUnit.containsClass(relationItem.getDepend())) {
				detail.add(relationItem);
			}
		}
		return detail;
	}

	@Override
	public float caCoupling() {
		float intensity = 0;
		for (JavaClassRelationItem relationItem : this.caItems) {
			if (!this.component.containsClass(relationItem.getDepend())) {
				intensity += relationItem.calRelationIntensity();
			}
		}
		return intensity;
	}

	@Override
	public float ceCoupling() {
		float intensity = 0;
		for (JavaClassRelationItem relationItem : this.ceItems) {
			if (!this.component.containsClass(relationItem.getDepend())) {
				if (!relationItem.getDepend().isStable()) {
					intensity += relationItem.calRelationIntensity();
				}
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
		if (this.cohesion == null) {
			float intensity = 0;
			for (JavaClassRelationItem relationItem : this.getCeItems()) {
				if (this.component.containsClass(relationItem.getDepend())) {
					intensity += relationItem.calRelationIntensity();
				}
			}
			for (JavaClassRelationItem relationItem : this.getCaItems()) {
				if (this.component.containsClass(relationItem.getDepend())) {
					intensity += relationItem.calRelationIntensity();
				}
			}

			this.cohesion = intensity;
		}
		return this.cohesion;
	}

	@Override
	public float getBalance() {
		if (this.balance == null) {
			float numerator = this.getCohesion();
			float denominatorPart = this.getGroupCouplingInfo().getMaxDifference();
			if (numerator == 0F) {
				if (denominatorPart == 0F) {
					this.balance = 0.5F;
				} else {
					this.balance = 0F;
				}
			} else {
				this.balance = numerator / (numerator + denominatorPart);
			}
		}
		return this.balance;
	}

	public GroupCouplingInfo getGroupCouplingInfo() {
		if (this.groupCouplingInfo == null) {
			GroupCouplingMaxDifferenceCalculator calculator = new GroupCouplingMaxDifferenceCalculator(this);
			GroupCouplingInfo info = new GroupCouplingInfo();
			info.setGroupCouplingItems(calculator.getGroupCouplingItems());
			info.setDifferences(calculator.getDifferences());
			info.setMaxDifference(calculator.getMaxDifference());

			this.groupCouplingInfo = info;
		}
		return this.groupCouplingInfo;
	}

	@Override
	public float getObjectOriented() {
		if (this.objectOriented == null) {
			int attributeCount = this.countAttributes();
			int methodCount = this.countMethods();
			if (methodCount != 0) {
				this.objectOriented = new Float(attributeCount) / new Float(methodCount);
			} else {
				this.objectOriented = 0F;
			}
		}
		return this.objectOriented;
	}

	/**
	 * 计算非公开属性
	 * 
	 * @return
	 */
	protected int countAttributes() {
		int count = 0;
		for (Attribute attribute : this.getAttributes()) {
			if (!attribute.isPublic() && !attribute.isStatic()) {
				count += 1;
			}
		}
		return count;
	}

	/**
	 * 计算公开方法
	 * 
	 * @return
	 */
	protected int countMethods() {
		int count = 0;
		for (Method method : this.getSelfMethods()) {
			if (method.isPublic() && !method.isConstruction()) {
				count += 1;
			}
		}
		return count;
	}

	@Override
	public int collectCycle(List<JDependUnit> list, Map<JDependUnit, Integer> knowledge) {

		if (list.contains(this)) {
			if (list.get(0).equals(this)) {
				for (JDependUnit unit : list) {
					if (!this.component.containsClass((JavaClass) unit)) {
						return Cycle;// 存在循环依赖
					}
				}
			} else {
				knowledge.put(this, 1);
				return LocalCycle;// 存在局部循环依赖
			}
		}

		list.add(this);// 将当前分析单元入站

		if (this.getCeList().contains(list.get(0))) {// 直接依赖进行广度搜索
			for (JDependUnit unit : list) {
				if (!this.component.containsClass((JavaClass) unit)) {
					return Cycle;// 存在循环依赖
				}
			}
		}

		for (JavaClass efferent : this.getCeList()) {
			Integer rtnInteger = (Integer) knowledge.get(efferent);// 获取历史扫描数据
			if (rtnInteger == null) {// 没有扫描过的区域进行深度扫描
				int rtn = efferent.collectCycle(list, knowledge);// 深度搜索该区域
				if (rtn == Cycle) {// 存在循环依赖
					return Cycle;
				}
			}
		}

		list.remove(this);// 将当前分析单元出站

		knowledge.put(this, NoCycle);// 记录该对象扫描过的结果

		return NoCycle;// 不存在循环依赖
	}

	public boolean haveState() {
		if (this.haveState == UnCalculate) {
			this.haveState = this.searchState();
		}

		return this.haveState == HaveState;
	}

	public void setState(boolean haveState) {
		if (haveState) {
			this.haveState = HaveState;
		} else {
			this.haveState = NoHaveState;
		}
	}

	@Override
	public JavaClass clone(Collection<JavaClass> javaClasses) throws JDependException {
		throw new JDependException("JavaClass不能够作为组件进行克隆");
	}

	@Override
	public Collection<JavaPackage> getJavaPackages() {
		Collection<JavaPackage> javaPackages = new ArrayList<JavaPackage>();
		javaPackages.add(javaPackage);
		return javaPackages;
	}

	public JavaPackage getJavaPackage() {
		return javaPackage;
	}

	public void setJavaPackage(JavaPackage javaPackage) {
		this.javaPackage = javaPackage;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public void setStable(boolean b) {
		this.stable = b;
	}

	public boolean isStable() {
		return stable;
	}

	public boolean isIncludeTransactionalAnnotation() {
		return isIncludeTransactionalAnnotation;
	}

	public void setIncludeTransactionalAnnotation(boolean isIncludeTransactionalAnnotation) {
		this.isIncludeTransactionalAnnotation = isIncludeTransactionalAnnotation;
	}

	@Override
	public int getClassCount() {
		return 1;
	}

	@Override
	public Collection<JavaClass> getClasses() {
		Collection<JavaClass> javaClasses = new ArrayList<JavaClass>();
		javaClasses.add(this);
		return javaClasses;
	}

	@Override
	public boolean containsClass(JavaClass javaClass) {
		return this.equals(javaClass);
	}

	@Override
	public void addRelation(Relation relation) {
		throw new RuntimeException("该方法不可用");
	}

	public JavaClass clone() {

		JavaClass obj = new JavaClass(this.getName(), this.isInner);

		obj.access_flags = this.access_flags;
		obj.packageName = this.packageName;
		obj.lineCount = this.lineCount;
		obj.abstractClassQualification = this.abstractClassQualification;
		obj.stable = this.stable;
		obj.isIncludeTransactionalAnnotation = this.isIncludeTransactionalAnnotation;

		obj.setState(this.haveState());
		obj.setType(this.getType());

		for (String importPackage : this.getImportedPackages()) {
			obj.addImportedPackage(importPackage);
		}

		JavaClassDetail sourceDetail = this.getDetail();
		JavaClassDetail targetDetail = new JavaClassDetail(obj);

		targetDetail.setSuperClassName(sourceDetail.getSuperClassName());

		for (Attribute name : sourceDetail.getAttributes()) {
			targetDetail.addAttribute(new Attribute(name));
		}

		for (String name : sourceDetail.getInterfaceNames()) {
			targetDetail.addInterfaceName(name);
		}

		for (String name : sourceDetail.getVariableTypes()) {
			targetDetail.addVariableType(name);
		}

		for (TableInfo tableRelationInfo : sourceDetail.getTables()) {
			targetDetail.addTable(new TableInfo(tableRelationInfo));
		}

		obj.setDetail(targetDetail);

		for (JavaClassRelationItem item : this.getCaItems()) {
			JavaClassRelationItem newItem = new JavaClassRelationItem();

			newItem.setDirection(item.getDirection());
			newItem.setType(item.getType());

			newItem.setCurrentJavaClass(item.getCurrent().getName());
			newItem.setDependJavaClass(item.getDepend().getName());

			obj.addCaItems(newItem);
		}

		for (JavaClassRelationItem item : this.getCeItems()) {
			JavaClassRelationItem newItem = new JavaClassRelationItem();

			newItem.setDirection(item.getDirection());
			newItem.setType(item.getType());

			newItem.setCurrentJavaClass(item.getCurrent().getName());
			newItem.setDependJavaClass(item.getDepend().getName());

			obj.addCeItems(newItem);
		}

		return obj;
	}

	@Override
	public int compareTo(JDependUnit o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 32;
		int result = 1;
		result = prime * result + ((this.getName() == null) ? 0 : this.getName().hashCode());
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
		JavaClass other = (JavaClass) obj;
		if (this.getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!this.getName().equals(other.getName()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.detail.toString();
	}

	@Override
	public void clear() {
		super.clear();

		caList = null;
		ceList = null;
		relationList = null;
		afferents = null;
		efferents = null;
		cohesion = null;
		balance = null;
		objectOriented = null;
		groupCouplingInfo = null;
	}

	public final boolean isPublic() {
		return (access_flags & Constants.ACC_PUBLIC) != 0;
	}

	public final boolean isPrivate() {
		return (access_flags & Constants.ACC_PRIVATE) != 0;
	}

	public final boolean isProtected() {
		return (access_flags & Constants.ACC_PROTECTED) != 0;
	}

	public final boolean isStatic() {
		return (access_flags & Constants.ACC_STATIC) != 0;
	}

	public final boolean isFinal() {
		return (access_flags & Constants.ACC_FINAL) != 0;
	}

	public final boolean isInterface() {
		return (access_flags & Constants.ACC_INTERFACE) != 0;
	}

	public final boolean isAbstract() {
		return (access_flags & Constants.ACC_ABSTRACT) != 0;
	}

	public final boolean isAnnotation() {
		return (access_flags & Constants.ACC_ANNOTATION) != 0;
	}

	public final boolean isEnum() {
		return (access_flags & Constants.ACC_ENUM) != 0;
	}

	/**
	 * 是否存在可变基本类型
	 * 
	 * @param types
	 * @return
	 */
	private static boolean exitChangeBaseType(Collection<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			if (!attribute.isFinal() && !attribute.isStatic()) {
				for (String baseType : baseTypes) {
					if (attribute.getInfo().indexOf(baseType) != -1) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public Collection<JavaClass> getInvokeClasses() {
		if (this.invokeClasses == null) {
			this.invokeClasses = new HashSet<JavaClass>();
			this.collectInvokeClasses(this, invokeClasses);
		}
		return this.invokeClasses;

	}

	private void collectInvokeClasses(JavaClass javaClass, Collection<JavaClass> invokeClasses) {
		JavaClass invokeClass;
		for (Method method : javaClass.getSelfMethods()) {
			for (InvokeItem invokeItem : method.getInvokeItems()) {
				invokeClass = invokeItem.getMethod().getJavaClass();
				if (!invokeClasses.contains(invokeClass)) {
					invokeClasses.add(invokeClass);
					collectInvokeClasses(invokeClass, invokeClasses);
				}
			}
		}
	}

	/**
	 * 搜索该JavaClass是否存在状态
	 * 
	 * @return 1 存在；0 不存在； -1 该javaClass已经扫描过了。
	 */
	private int searchState() {

		// 判断该javaClass是否扫描过
		Collection<JavaClass> javaClasses = (Collection<JavaClass>) JDependContext.getInfo(SCOPE.THREAD_SCOPSE,
				haveStateJavaClasses);
		if (javaClasses == null) {// 建立历史列表
			javaClasses = new HashSet<JavaClass>();
			JDependContext.setInfo(SCOPE.THREAD_SCOPSE, haveStateJavaClasses, javaClasses);
		} else if (javaClasses.contains(this)) {// 扫描过
			return Searched;
		}
		// 放入扫描列表
		javaClasses.add(this);

		// 判断是否存在可变基本类型
		if (exitChangeBaseType(this.getAttributes())) {
			// 清空扫描列表
			JDependContext.setInfo(SCOPE.THREAD_SCOPSE, haveStateJavaClasses, null);
			return HaveState;
		}

		// 收集属性数据
		List<JavaClassRelationItem> fieldRelationItems = new ArrayList<JavaClassRelationItem>();
		for (JavaClassRelationItem item : this.ceItems) {
			if (item.getType() instanceof FieldRelation) {
				fieldRelationItems.add(item);
			}
		}

		if (this.detail.getAttributeTypes().size() > fieldRelationItems.size()) {
			// 清空扫描列表
			JDependContext.setInfo(SCOPE.THREAD_SCOPSE, haveStateJavaClasses, null);
			return HaveState;
		}

		int result;

		// 判断属性
		for (JavaClassRelationItem item : fieldRelationItems) {
			result = item.getDepend().searchState();
			if (result != Searched) {
				return result;
			}
		}

		// 判断父类是否存在状态
		if (this.getSuperClass() != null) {
			result = this.getSuperClass().searchState();
			if (result != Searched) {
				return result;
			}
		}

		if (javaClasses.iterator().next().equals(this)) {// 搜索完毕时，清空搜索列表
			JDependContext.setInfo(SCOPE.THREAD_SCOPSE, haveStateJavaClasses, null);
		}
		return NoHaveState;
	}

	public void calImportedPackages() {
		String packageName;

		if (this.detail.getSuperClassName() != null) {
			packageName = ParseUtil.getPackageName(this.detail.getSuperClassName());
			addImportedPackage(packageName);
		}

		for (String name : this.detail.getInterfaceNames()) {
			packageName = ParseUtil.getPackageName(name);
			addImportedPackage(packageName);
		}

		for (String name : this.detail.getAttributeTypes()) {
			packageName = ParseUtil.getPackageName(name);
			addImportedPackage(packageName);
		}

		for (String name : this.detail.getParamTypes()) {
			packageName = ParseUtil.getPackageName(name);
			addImportedPackage(packageName);
		}

		for (String name : this.detail.getVariableTypes()) {
			packageName = ParseUtil.getPackageName(name);
			addImportedPackage(packageName);
		}
	}

	private boolean addImportedPackage(String jPackage) {
		if (jPackage != null) {
			if (!jPackage.equals(packageName) && !imports.contains(jPackage)) {
				imports.add(jPackage);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
