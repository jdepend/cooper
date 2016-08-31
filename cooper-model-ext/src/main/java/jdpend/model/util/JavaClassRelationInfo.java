package jdpend.model.util;

public class JavaClassRelationInfo implements Comparable<JavaClassRelationInfo> {

	public String current;
	public String depend;
	public String type;
	public boolean isInner;
	public float intensity;

	public JavaClassRelationInfo(String current, String depend, String type, boolean isInner, float intensity) {
		super();
		this.current = current;
		this.depend = depend;
		this.type = type;
		this.isInner = isInner;
		this.intensity = intensity;
	}

	@Override
	public int compareTo(JavaClassRelationInfo o) {
		return this.current.compareTo(o.current);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((current == null) ? 0 : current.hashCode());
		result = prime * result + ((depend == null) ? 0 : depend.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		JavaClassRelationInfo other = (JavaClassRelationInfo) obj;
		if (current == null) {
			if (other.current != null)
				return false;
		} else if (!current.equals(other.current))
			return false;
		if (depend == null) {
			if (other.depend != null)
				return false;
		} else if (!depend.equals(other.depend))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
