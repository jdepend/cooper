package jdepend.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;

/**
 * 指标计算针对的单元
 * 
 * @author <b>Abner</b>
 * 
 */
public interface JDependUnit extends Serializable, Measurable, Comparable<JDependUnit> {

	/**
	 * 该分析单元是否是内部对象
	 * 
	 * @return
	 */
	public boolean isInner();

	/**
	 * 得到类型
	 * 
	 * @return
	 */
	public JDependUnitType getType();

	public void setType(JDependUnitType type);

	public Collection<JavaClass> getClasses();

	public boolean containsClass(JavaClass javaClass);

	public Collection<JavaPackage> getJavaPackages();

	/**
	 * 得到该组件的路径（根包名称）
	 * 
	 * @return
	 */
	public String getPath();

	public Collection<? extends JDependUnit> getAfferents();

	public Collection<? extends JDependUnit> getEfferents();

	/**
	 * 收集循环依赖信息
	 * 
	 * @param list
	 * @param knowledge
	 * @return
	 */
	int collectCycle(List<JDependUnit> list, Map<JDependUnit, Integer> knowledge);

	/**
	 * 收集第一个循环依赖链
	 * 
	 * @return
	 */
	public List<JDependUnit> collectCycle();

	/**
	 * 与特定分析单元的传出耦合值
	 * 
	 * @param dependUnit
	 * @return
	 */
	public float ceCoupling(JDependUnit dependUnit);

	/**
	 * 与特定分析单元的传出耦合信息明细
	 * 
	 * @param dependUnit
	 * @return
	 */
	public Collection<JavaClassRelationItem> ceCouplingDetail(JDependUnit dependUnit);

	/**
	 * 与特定分析单元的传入耦合值
	 * 
	 * @param dependUnit
	 * @return
	 */
	public float caCoupling(JDependUnit dependUnit);

	/**
	 * 与特定分析单元的传入耦合信息明细
	 * 
	 * @param dependUnit
	 * @return
	 */
	public Collection<JavaClassRelationItem> caCouplingDetail(JDependUnit dependUnit);

	/**
	 * 与特定分析单元的耦合值
	 * 
	 * @param dependUnit
	 * @return
	 */
	public float coupling(JDependUnit dependUnit);

	/**
	 * 传入耦合值
	 * 
	 * @return
	 */
	public float caCoupling();

	/**
	 * 传出耦合值
	 * 
	 * @return
	 */
	public float ceCoupling();

	public void addRelation(Relation relation);

	/**
	 * 克隆分析单元
	 */
	public JDependUnit clone(Collection<JavaClass> javaClasses) throws JDependException;

	/**
	 * 清空缓存
	 */
	public void clear();

	public int hashCode();

	public boolean equals(Object obj);

	/**
	 * 计算与特定分析单元的传出耦合值
	 * 
	 * @param jdependUnit
	 * @return
	 */
	public float calCeCoupling(JDependUnit jdependUnit);

	/**
	 * 计算与特定分析单元的传出耦合细节信息
	 * 
	 * @param jDependUnit
	 * @return
	 */
	public Collection<JavaClassRelationItem> calCeCouplingDetail(JDependUnit jDependUnit);
}
