package jdepend.metadata.relationtype;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.metadata.JavaClassRelationType;
import jdepend.metadata.util.JavaClassCollection;

public abstract class BaseJavaClassRelationType implements JavaClassRelationType {

	private String name;

	private float intensity = 0F;

	private transient JavaClassRelationTypes types;

	public BaseJavaClassRelationType() {

	}

	public BaseJavaClassRelationType(String name, float intensity) {
		this.name = name;
		this.intensity = intensity;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public void init(JavaClassCollection javaClasses) {
	}

	protected boolean setDependInfo(JavaClass source, JavaClass target, JavaClassRelationType type) {

		if (target == null) {
			return false;
		}

		if (source.equals(target)) {
			return false;
		}

		if (source.containsInnerClass(target)) {
			return false;
		}

		if (source.containedInnerClass(target)) {
			return false;
		}

		JavaClassRelationItem item = new JavaClassRelationItem();
		item.setType(type);
		item.setTarget(target);
		item.setSource(source);

		source.addCeItems(item);
		target.addCaItems(item);

		return true;
	}

	public JavaClassRelationTypes getTypes() {
		return types;
	}

	public void setTypes(JavaClassRelationTypes types) {
		this.types = types;
	}

	@Override
	public String toString() {
		return "JavaClassRelationType[intensity=" + intensity + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (getClass() != obj.getClass())
			return false;
		BaseJavaClassRelationType other = (BaseJavaClassRelationType) obj;

		if (!name.equals(other.name))
			return false;

		return true;
	}

}
