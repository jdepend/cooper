package jdepend.metadata.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationItem;

public abstract class JavaClassTreeCreator {

	private Collection<JavaClass> javaClasses = new HashSet<JavaClass>();// 记录扫描过的JavaClass

	JavaClassTreeCreator() {
	}

	public JavaClassTree create(JavaClass rootClass) {
		JavaClassTree tree = new JavaClassTree(rootClass);
		javaClasses.add(rootClass);
		this.rout(rootClass, tree);
		return tree;
	}

	private void rout(JavaClass javaClass, JavaClassTree tree) {

		List<JavaClass> dependClasses = new ArrayList<JavaClass>();
		// 广度搜索
		for (JavaClass dependClass : getRelationClass(javaClass)) {
			if (classes.contains(dependClass)) {
				if (!javaClasses.contains(dependClass)) {
					javaClasses.add(dependClass);
					dependClasses.add(dependClass);
					tree.addNode(javaClass, dependClass);
				}
			}
		}
		// 深度搜索
		for (JavaClass dependClass1 : dependClasses) {
			rout(dependClass1, tree, classes);
		}
	}

	/**
	 * 得到指定与该类有关系的类集合
	 * 
	 * @param javaClass
	 * @return
	 */
	protected abstract Collection<JavaClass> getRelationClass(JavaClass javaClass);
}
