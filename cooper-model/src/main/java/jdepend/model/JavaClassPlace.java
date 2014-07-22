package jdepend.model;

import java.io.Serializable;

public final class JavaClassPlace implements Serializable {

	private static final long serialVersionUID = 6921367635573892005L;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
