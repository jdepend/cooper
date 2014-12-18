package jdepend.util.analyzer.element.layer;

public class LayerInfo {

	public String name;

	public String superClassName;

	public String packageEndsWith;

	public LayerInfo(String name, String superClassName, String packageEndsWith) {
		super();

		this.name = name;
		this.superClassName = superClassName;
		this.packageEndsWith = packageEndsWith;
	}

}
