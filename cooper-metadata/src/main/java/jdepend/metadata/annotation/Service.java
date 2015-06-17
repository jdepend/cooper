package jdepend.metadata.annotation;

import java.io.Serializable;

public class Service implements Serializable {

	private static final long serialVersionUID = -7821491389424291875L;

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		if (value != null) {
			return "Service [value=" + value + "]";
		} else {
			return "Service";
		}
	}
}
