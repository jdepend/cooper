package jdepend.metadata.tree;

public class TreeCreatorFactory {

	public static JavaClassTreeCreator createJavaClassCaTreeCreator() {
		return new JavaClassCaTreeCreator();
	}

	public static JavaClassTreeCreator createJavaClassCeTreeCreator() {
		return new JavaClassCeTreeCreator();
	}

	public static JavaClassTreeCreator createJavaClassAutowiredTreeCreator() {
		return new JavaClassAutowiredTreeCreator();
	}

	public static JavaClassTreesCreator createJavaClassFieldTreesCreator() {
		return new JavaClassFieldTreesCreator();
	}

	public static JavaClassTreesCreator createJavaClassInheritTreesCreator() {
		return new JavaClassInheritTreesCreator();
	}

}
