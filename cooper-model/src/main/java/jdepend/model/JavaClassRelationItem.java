package jdepend.model;

import java.io.IOException;
import java.io.Serializable;

import jdepend.framework.exception.JDependException;
import jdepend.model.component.modelconf.CandidateUtil;

/**
 * 两个JavaClass之间的关联项信息
 * 
 * @author <b>Abner</b>
 * 
 */
public class JavaClassRelationItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6332298811666212021L;

	private transient JavaClass target = null;// 目标javaclass

	private transient JavaClass source = null;// 源javaClass

	private String targetJavaClassPlace = null;// 序列化和反序列化时使用

	private String targetJavaClass = null;// 序列化和反序列化时使用

	private String sourceJavaClassPlace = null;// 序列化和反序列化时使用

	private String sourceJavaClass = null;// 序列化和反序列化时使用

	private transient JavaClassRelationType type = null;// 关联类型

	private String typeName = null; // 序列化和反序列化时使用

	/**
	 * 得到该关联项关联强度
	 * 
	 * @return
	 */
	public float getRelationIntensity() {
		return this.type.getIntensity();
	}

	/**
	 * 该关系是同一个组件内的关系
	 * 
	 * @return
	 */
	public boolean isInner() {
		return this.source.getComponent().equals(this.target.getComponent());
	}

	public JavaClass getTarget() {
		return target;
	}

	public void setTarget(JavaClass target) {
		this.target = target;
	}

	public JavaClassRelationType getType() {
		return type;
	}

	public String getTargetJavaClass() {
		return targetJavaClass;
	}

	public String getSourceJavaClass() {
		return sourceJavaClass;
	}

	public void setType(JavaClassRelationType type) {
		this.type = type;
	}

	public JavaClass getSource() {
		return source;
	}

	public void setSource(JavaClass source) {
		this.source = source;
	}

	public void setTargetJavaClass(String targetJavaClass) {
		this.targetJavaClass = targetJavaClass;
	}

	public void setSourceJavaClass(String sourceJavaClass) {
		this.sourceJavaClass = sourceJavaClass;
	}

	public String getTargetJavaClassPlace() {
		return targetJavaClassPlace;
	}

	public String getSourceJavaClassPlace() {
		return sourceJavaClassPlace;
	}

	public void setTargetJavaClassPlace(String targetJavaClassPlace) {
		this.targetJavaClassPlace = targetJavaClassPlace;
	}

	public void setSourceJavaClassPlace(String sourceJavaClassPlace) {
		this.sourceJavaClassPlace = sourceJavaClassPlace;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * 判断该关系是否是组件间的类关系
	 * 
	 * @return
	 */
	public boolean crossComponent() {
		return !this.source.getComponent().containsClass(this.target);
	}

	public JavaClassRelationItem clone() {
		JavaClassRelationItem newItem = new JavaClassRelationItem();

		newItem.type = this.type;
		newItem.typeName = this.typeName;

		newItem.sourceJavaClassPlace = this.sourceJavaClassPlace;
		newItem.sourceJavaClass = this.sourceJavaClass;

		newItem.targetJavaClassPlace = this.targetJavaClassPlace;
		newItem.targetJavaClass = this.targetJavaClass;

		return newItem;
	}

	/**
	 * 创建一个以宿主类为current的relationItem
	 * 
	 * @return
	 * @throws JDependException
	 */
	public JavaClassRelationItem clone2() {

		if (!this.source.isInnerClass()) {
			return this;
		} else {
			JavaClassRelationItem newItem = new JavaClassRelationItem();

			newItem.type = this.type;
			newItem.typeName = this.typeName;

			newItem.sourceJavaClassPlace = this.source.getHostClass().getPlace();
			newItem.sourceJavaClass = this.source.getHostClass().getName();
			newItem.source = this.source.getHostClass();

			newItem.targetJavaClassPlace = this.targetJavaClassPlace;
			newItem.targetJavaClass = this.targetJavaClass;
			newItem.target = this.target;

			return newItem;
		}
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {

		// 准备序列化数据
		this.targetJavaClassPlace = this.target.getPlace();
		this.targetJavaClass = this.target.getName();
		this.sourceJavaClassPlace = this.source.getPlace();
		this.sourceJavaClass = this.source.getName();
		this.typeName = this.type.getName();

		out.defaultWriteObject();// 序列化对象
	}

	@Override
	public String toString() {
		return "JavaClassRelationItem [source=" + source.getName() + ", target=" + target.getName() + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((source == null) ? CandidateUtil.getId(sourceJavaClassPlace, sourceJavaClass).hashCode()
						: source.getName().hashCode());
		result = prime
				* result
				+ ((target == null) ? CandidateUtil.getId(targetJavaClassPlace, targetJavaClass).hashCode() : target
						.getName().hashCode());
		result = prime * result + ((type == null) ? typeName.hashCode() : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		JavaClassRelationItem other = (JavaClassRelationItem) obj;

		if (source != null && other.source != null) {
			if (!source.equals(other.source))
				return false;
			if (!target.equals(other.target))
				return false;
		} else {
			if (!sourceJavaClassPlace.equals(other.sourceJavaClassPlace))
				return false;
			if (!targetJavaClassPlace.equals(other.targetJavaClassPlace))
				return false;
			if (!sourceJavaClass.equals(other.sourceJavaClass))
				return false;
			if (!targetJavaClass.equals(other.targetJavaClass))
				return false;
		}
		if (!type.equals(other.type))
			return false;

		return true;
	}
}
