package jdepend.knowledge.motive;

import java.io.Serializable;

public class Motive implements Serializable {

	private static final long serialVersionUID = 6470928091006082657L;

	private String desc;

	public Motive(String desc) {
		super();
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
