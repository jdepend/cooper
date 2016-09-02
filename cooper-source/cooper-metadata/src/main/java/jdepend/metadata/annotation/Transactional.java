package jdepend.metadata.annotation;


public class Transactional implements Annotation {

	private static final long serialVersionUID = 2692023348071298543L;

	private Boolean readOnly;

	private String propagation;

	private String value;

	public Boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getPropagation() {
		return propagation;
	}

	public void setPropagation(String propagation) {
		this.propagation = propagation;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {

		StringBuilder info = new StringBuilder();

		info.append("Transactional");
		if (readOnly != null) {
			info.append("[readOnly=");
			info.append(readOnly);
			info.append(",");
		}
		if (propagation != null) {
			info.append("[propagation=");
			info.append(propagation);
			info.append(",");
		}
		if (value != null) {
			info.append("[value=");
			info.append(value);
			info.append(",");
		}
		if (info.length() > 13) {
			info.delete(info.length() - 1, info.length()).append("]");
		}
		return info.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((propagation == null) ? 0 : propagation.hashCode());
		result = prime * result + ((readOnly == null) ? 0 : readOnly.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Transactional other = (Transactional) obj;
		if (propagation == null) {
			if (other.propagation != null)
				return false;
		} else if (!propagation.equals(other.propagation))
			return false;
		if (readOnly == null) {
			if (other.readOnly != null)
				return false;
		} else if (!readOnly.equals(other.readOnly))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
