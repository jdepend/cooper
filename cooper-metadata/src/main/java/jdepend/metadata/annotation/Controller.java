package jdepend.metadata.annotation;

import java.io.Serializable;

public class Controller implements Serializable {

	private static final long serialVersionUID = 6272481420978872027L;

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
			return "Controller [value=" + value + "]";
		} else {
			return "Controller";
		}
	}
}
