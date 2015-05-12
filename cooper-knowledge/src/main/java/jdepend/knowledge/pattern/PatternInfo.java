package jdepend.knowledge.pattern;

import jdepend.model.JavaClassUnit;

public class PatternInfo {

	private JavaClassUnit javaClass;

	private String info;

	public PatternInfo(JavaClassUnit javaClass, String info) {
		super();
		this.javaClass = javaClass;
		this.info = info;
	}

	public JavaClassUnit getJavaClass() {
		return javaClass;
	}

	public String getInfo() {
		return info;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((info == null) ? 0 : info.hashCode());
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
		PatternInfo other = (PatternInfo) obj;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (this.info == null) {
			return this.javaClass.getName();
		} else {
			return this.javaClass.getName() + "[" + info + "]";
		}
	}
}
