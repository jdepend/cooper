package jdepend.model;

import java.io.Serializable;

public class RequestMapping implements Serializable {

	private static final long serialVersionUID = 836148411426739295L;

	private String value;

	private String method;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String toString() {
		return "RequestMapping [value=" + value + ", method=" + method + "]";
	}
}
