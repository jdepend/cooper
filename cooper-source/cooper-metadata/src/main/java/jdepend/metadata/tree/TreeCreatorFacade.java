package jdepend.metadata.tree;

import java.util.Collection;
import java.util.List;

import jdepend.metadata.JavaClass;

public class TreeCreatorFacade {

	public static JavaClassTree createJavaClassCaTree(JavaClass rootClass) {
		return new JavaClassCaTreeCreator().create(rootClass);
	}

	public static JavaClassTree createJavaClassCeTree(JavaClass rootClass) {
		return new JavaClassCeTreeCreator().create(rootClass);
	}

	public static JavaClassTree createJavaClassAutowiredTree(JavaClass rootClass) {
		return new JavaClassAutowiredTreeCreator().create(rootClass);
	}

	public static List<JavaClassTree> createJavaClassFieldTrees(Collection<JavaClass> classes) {
		return new JavaClassFieldTreesCreator().create(classes);
	}

	public static List<JavaClassTree> createJavaClassInheritTrees(Collection<JavaClass> classes) {
		return new JavaClassInheritTreesCreator().create(classes);
	}

}
