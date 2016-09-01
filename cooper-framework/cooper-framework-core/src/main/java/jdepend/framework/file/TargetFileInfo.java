package jdepend.framework.file;

import java.io.Serializable;

public class TargetFileInfo implements Serializable {

	private static final long serialVersionUID = 3712368678061848786L;

	private String name;
	private String type;
	private byte[] content;

	public final static String TYPE_CLASS = "CLASS";
	public final static String TYPE_XML = "XML";

	public TargetFileInfo() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}
