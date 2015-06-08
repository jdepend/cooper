package jdepend.metadata.tree;

import java.util.Collection;
import java.util.HashSet;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationItem;

public abstract class JavaClassTreeCreator {

	private Collection<JavaClass> javaClasses = new HashSet<JavaClass>();// 记录扫描过的JavaClass

	JavaClassTreeCreator() {
	}

	public JavaClassTree create(JavaClass rootClass, Collection<JavaClass> classes) {
		JavaClassTree tree = new JavaClassTree(rootClass);
		javaClasses.add(rootClass);
		this.rout(rootClass, tree, classes);
		return tree;
	}

	private void rout(JavaClass javaClass, JavaClassTree tree, Collection<JavaClass> classes) {

		JavaClass dependClass = null;
		Collection<JavaClass> dependClasses = new HashSet<JavaClass>();
		// 广度搜索
		for (JavaClassRelationItem relationItem : getRelationItem(javaClass)) {
			dependClass = this.getDepend(relationItem);
			if (classes.contains(dependClass)) {
				if (!javaClasses.contains(dependClass)) {
					javaClasses.add(dependClass);
					dependClasses.add(dependClass);
					tree.addNode(javaClass, dependClass);
				}
			}
		}
		// 深度搜索
		for (JavaClassRelationItem relationItem : getRelationItem(javaClass)) {
			dependClass = this.getDepend(relationItem);
			if (dependClasses.contains(dependClass)) {
				rout(dependClass, tree, classes);
			}
		}
	}

	/**
	 * 得到指定JavaClassRelationType的关系信息集合
	 * 
	 * @param javaClass
	 * @return
	 */
	protected abstract Collection<JavaClassRelationItem> getRelationItem(JavaClass javaClass);

	/**
	 * 得到JavaClassRelationItem中的对方
	 * 
	 * @param item
	 * @return
	 */
	protected abstract JavaClass getDepend(JavaClassRelationItem item);

	/**
	 * 得到JavaClassRelationItem中的自己
	 * 
	 * @param item
	 * @return
	 */
	protected abstract JavaClass getCurrent(JavaClassRelationItem item);

}
