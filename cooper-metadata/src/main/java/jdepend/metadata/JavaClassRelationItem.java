package jdepend.metadata;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import jdepend.framework.exception.JDependException;

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

	private String id;

	private transient JavaClass target = null;// 目标javaclass

	private transient JavaClass source = null;// 源javaClass

	private String targetJavaClassPlace = null;// 序列化和反序列化时使用

	private String targetJavaClass = null;// 序列化和反序列化时使用

	private String sourceJavaClassPlace = null;// 序列化和反序列化时使用

	private String sourceJavaClass = null;// 序列化和反序列化时使用

	private transient JavaClassRelationType type = null;// 关联类型

	private String typeName = null; // 序列化和反序列化时使用

	public JavaClassRelationItem() {
		super();
		this.id = UUID.randomUUID().toString();
	}

	/**
	 * 得到该关联项关联强度
	 * 
	 * @return
	 */
	public float getRelationIntensity() {
		return this.type.getIntensity();
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Collection<InvokeItem> getInvokeDetail() {
		Collection<InvokeItem> invokedItems = new HashSet<InvokeItem>();
		if (this.type.invokeRelated()) {
			for (Method method : target.getMethods()) {
				for (InvokeItem invokeItem : method.getInvokedItems()) {
					if (invokeItem.getCaller().getJavaClass().equals(source)) {
						invokedItems.add(invokeItem);
					}
				}
			}
		}
		return invokedItems;
	}

	public JavaClassRelationItem clone() {
		JavaClassRelationItem newItem = new JavaClassRelationItem();

		newItem.id = this.id;
		newItem.type = this.type;
		newItem.typeName = this.type.getName();

		newItem.sourceJavaClassPlace = this.source.getPlace();
		newItem.sourceJavaClass = this.source.getName();

		newItem.targetJavaClassPlace = this.target.getPlace();
		newItem.targetJavaClass = this.target.getName();

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
			JavaClass hostClass = this.source.getHostClass();
			if (hostClass != null) {
				JavaClassRelationItem newItem = new JavaClassRelationItem();

				newItem.type = this.type;
				newItem.typeName = this.typeName;

				newItem.sourceJavaClassPlace = hostClass.getPlace();
				newItem.sourceJavaClass = hostClass.getName();
				newItem.source = hostClass;

				newItem.targetJavaClassPlace = this.targetJavaClassPlace;
				newItem.targetJavaClass = this.targetJavaClass;
				newItem.target = this.target;

				return newItem;
			} else {
				return this;
			}
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
		return "JavaClassRelationItem [source=" + source.getName() + ", target=" + target.getName() + ", type=" + type
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		JavaClassRelationItem other = (JavaClassRelationItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
