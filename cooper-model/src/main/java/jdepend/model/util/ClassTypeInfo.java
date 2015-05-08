package jdepend.model.util;

public class ClassTypeInfo {

	private String type;
	private String path;

	public ClassTypeInfo(String type, String path) {
		super();
		this.type = type;
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return type + path;
	}

}
