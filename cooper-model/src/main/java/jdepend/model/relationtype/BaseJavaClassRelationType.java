package jdepend.model.relationtype;

import jdepend.model.JavaClassUnit;
import jdepend.model.JavaClassRelationType;

public abstract class BaseJavaClassRelationType implements JavaClassRelationType {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5670515208747517988L;

	private String name;

	private float intensity = 0F;

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

	public float getRationality(JavaClassUnit depend, JavaClassUnit current, String direction) {
		return 1F;
	}

	public String getName() {
		return this.name;
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
