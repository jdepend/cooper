package jdepend.metadata.annotation;

public class Autowired implements Annotation {

	private static final long serialVersionUID = 829694821909277252L;

	private Boolean required;

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	@Override
	public String toString() {
		if (required != null) {
			return "Autowired [required=" + required + "]";
		} else {
			return "Autowired";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((required == null) ? 0 : required.hashCode());
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
		Autowired other = (Autowired) obj;
		if (required == null) {
			if (other.required != null)
				return false;
		} else if (!required.equals(other.required))
			return false;
		return true;
	}
}
