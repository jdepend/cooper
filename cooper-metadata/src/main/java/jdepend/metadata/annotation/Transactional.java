package jdepend.metadata.annotation;

import java.io.Serializable;

public class Transactional implements Serializable {

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

}
