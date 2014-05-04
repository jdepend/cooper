package jdepend.model.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.JavaClassRelationType;
import jdepend.model.relationtype.InheritRelation;

public abstract class JavaClassTreeCreator {

	private Collection<JavaClass> javaClasses = new HashSet<JavaClass>();// 记录扫描过的JavaClass

	private List<JavaClassTree> trees;

	private JavaClassRelationType type;

	JavaClassTreeCreator(JavaClassRelationType type) {
		this.type = type;
	}

	public List<JavaClassTree> create(Collection<JavaClass> classes) {

		trees = new ArrayList<JavaClassTree>();

		JavaClassTree tree;

		boolean unite = false;// 合并到其他树上

		for (JavaClass javaClass : classes) {
			javaClasses.contains(javaClass);
			unite = false;
			for (JavaClassRelationItem relationItem : this.getRelationItem(javaClass)) {
				if (!javaClasses.contains(relationItem.getDepend()) && classes.contains(relationItem.getDepend())) {
					javaClasses.add(relationItem.getDepend());
					// 判断是否要合并到其它树上
					for (JavaClassTree currentTree : trees) {
						if (currentTree.contains(relationItem.getDepend())) {
							if (currentTree.getJavaClassRoots().contains(relationItem.getDepend())) {
								currentTree.setRoot(relationItem.getCurrent(), relationItem.getDepend());
							} else {
								currentTree.insertNode(relationItem.getCurrent(), relationItem.getDepend());
							}
							unite = true;
						}
					}
					if (unite)
						break;
					// 创建新的树
					tree = new JavaClassTree(javaClass);
					rout(tree, classes);// 扫描JavaClass，增加节点
					trees.add(tree);
				}
			}
		}

		// 删除只有一个节点的树
		Iterator<JavaClassTree> it = trees.iterator();
		JavaClassTree tree1;
		while (it.hasNext()) {
			tree1 = it.next();
			if (tree1.getDeep() == 1) {
				it.remove();
			}
		}

		return trees;
	}

	private void rout(JavaClassTree tree, Collection<JavaClass> classes) {

		JavaClass currentClass = tree.getCurrent();
		javaClasses.add(currentClass);
		for (JavaClassRelationItem relationItem : getRelationItem(currentClass)) {// 广度搜索
			if (classes.contains(relationItem.getDepend())) {
				if (!javaClasses.contains(relationItem.getDepend())) {
					javaClasses.add(relationItem.getDepend());
					tree.addNode(relationItem.getCurrent(), relationItem.getDepend());
					rout(tree, classes);// 深度搜索
				} else {
					// 判断是否要合并到其它树上
					Iterator<JavaClassTree> it = trees.iterator();
					JavaClassTree currentTree;
					while (it.hasNext()) {
						currentTree = it.next();
						if (currentTree.contains(relationItem.getDepend())) {
							if (currentTree.getJavaClassRoots().contains(relationItem.getDepend())) {
								tree.appendTree(currentClass, currentTree);
								it.remove();
							} else {
								// 只对继承关系进行合并
								if (this.type instanceof InheritRelation) {
									tree.mergeTree(currentTree, currentClass, relationItem.getDepend());
									it.remove();
								}
							}
						}
					}
				}
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
}
