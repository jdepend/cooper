package jdepend.model;

import java.io.IOException;
import java.io.Serializable;

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

	public static final String CA_DIRECTION = "CA";

	public static final String CE_DIRECTION = "CE";

	private transient JavaClass depend = null;// 关联的javaclass

	private transient JavaClass current = null;// 当前javaClass

	private String dependJavaClassPlace = null;// 序列化和反序列化时使用

	private String dependJavaClass = null;// 序列化和反序列化时使用

	private String currentJavaClassPlace = null;// 序列化和反序列化时使用

	private String currentJavaClass = null;// 序列化和反序列化时使用

	private transient JavaClassRelationType type = null;// 关联类型

	private String typeName = null; // 序列化和反序列化时使用

	private String direction = null;// 关联方向

	/**
	 * 得到该关联项关联强度
	 * 
	 * @return
	 */
	public float getRelationIntensity() {
		// 计算耦合
		// float intensity = this.type.getIntensity();
		// float rationality = this.type.getRationality(this.depend,
		// this.current, this.direction);
		//
		// return intensity * rationality;
		return this.type.getIntensity();
	}

	public boolean isInner() {
		return this.current.getComponent().equals(this.depend.getComponent());
	}

	public JavaClass getDepend() {
		return depend;
	}

	public void setDepend(JavaClass depend) {
		this.depend = depend;
	}

	public JavaClassRelationType getType() {
		return type;
	}

	public String getDependJavaClass() {
		return dependJavaClass;
	}

	public String getCurrentJavaClass() {
		return currentJavaClass;
	}

	public void setType(JavaClassRelationType type) {
		this.type = type;
	}

	public JavaClass getCurrent() {
		return current;
	}

	public void setCurrent(JavaClass current) {
		this.current = current;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public void setDependJavaClass(String dependJavaClass) {
		this.dependJavaClass = dependJavaClass;
	}

	public void setCurrentJavaClass(String currentJavaClass) {
		this.currentJavaClass = currentJavaClass;
	}

	public String getDependJavaClassPlace() {
		return dependJavaClassPlace;
	}

	public String getCurrentJavaClassPlace() {
		return currentJavaClassPlace;
	}

	public void setDependJavaClassPlace(String dependJavaClassPlace) {
		this.dependJavaClassPlace = dependJavaClassPlace;
	}

	public void setCurrentJavaClassPlace(String currentJavaClassPlace) {
		this.currentJavaClassPlace = currentJavaClassPlace;
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
		return !this.current.getComponent().containsClass(this.depend);
	}

	public JavaClassRelationItem clone() {
		JavaClassRelationItem newItem = new JavaClassRelationItem();

		newItem.direction = this.direction;
		newItem.type = this.type;
		newItem.typeName = this.typeName;

		newItem.currentJavaClassPlace = this.currentJavaClassPlace;
		newItem.currentJavaClass = this.currentJavaClass;

		newItem.dependJavaClassPlace = this.dependJavaClassPlace;
		newItem.dependJavaClass = this.dependJavaClass;

		return newItem;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {

		// 准备序列化数据
		this.dependJavaClassPlace = this.depend.getPlace();
		this.dependJavaClass = this.depend.getName();
		this.currentJavaClassPlace = this.current.getPlace();
		this.currentJavaClass = this.current.getName();
		this.typeName = this.type.getName();

		out.defaultWriteObject();// 序列化对象
	}

	@Override
	public String toString() {
		return "JavaClassRelationItem [current=" + current.getName() + ", depend=" + depend.getName() + ", direction="
				+ direction + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((current == null) ? CandidateUtil.getId(currentJavaClassPlace, currentJavaClass).hashCode()
						: current.getName().hashCode());
		result = prime
				* result
				+ ((depend == null) ? CandidateUtil.getId(dependJavaClassPlace, dependJavaClass).hashCode() : depend
						.getName().hashCode());
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
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

		if (current != null && other.current != null) {
			if (!current.equals(other.current))
				return false;
			if (!depend.equals(other.depend))
				return false;
		} else {
			if (!currentJavaClassPlace.equals(other.currentJavaClassPlace))
				return false;
			if (!dependJavaClassPlace.equals(other.dependJavaClassPlace))
				return false;
			if (!currentJavaClass.equals(other.currentJavaClass))
				return false;
			if (!dependJavaClass.equals(other.dependJavaClass))
				return false;
		}
		if (!direction.equals(other.direction))
			return false;
		if (!type.equals(other.type))
			return false;

		return true;
	}
}
