package jdepend.metadata.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationItem;

public abstract class JavaClassTreeCreator {

	private Collection<JavaClass> javaClasses = new HashSet<JavaClass>();// 记录扫描过的JavaClass

	private List<JavaClassTree> trees;

	JavaClassTreeCreator() {
	}

	public List<JavaClassTree> create(Collection<JavaClass> classes) {

		trees = new ArrayList<JavaClassTree>();

		JavaClassTree tree;

		boolean unite = false;// 合并到其他树上

		for (JavaClass javaClass : classes) {
			javaClasses.add(javaClass);
			unite = false;
			for (JavaClassRelationItem relationItem : this.getRelationItem(javaClass)) {
				if (!javaClasses.contains(this.getDepend(relationItem))
						&& classes.contains(this.getDepend(relationItem))) {
					javaClasses.add(this.getDepend(relationItem));
					// 判断是否要合并到其它树上
					for (JavaClassTree currentTree : trees) {
						if (currentTree.contains(this.getDepend(relationItem))) {
							if (currentTree.getJavaClassRoots().contains(this.getDepend(relationItem))) {
								currentTree.setRoot(this.getCurrent(relationItem), this.getDepend(relationItem));
							} else {
								currentTree.insertNode(this.getCurrent(relationItem), this.getDepend(relationItem));
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
			if (classes.contains(this.getDepend(relationItem))) {
				if (!javaClasses.contains(this.getDepend(relationItem))) {
					javaClasses.add(this.getDepend(relationItem));
					tree.addNode(this.getCurrent(relationItem), this.getDepend(relationItem));
					rout(tree, classes);// 深度搜索
				} else {
					// 判断是否要合并到其它树上
					Iterator<JavaClassTree> it = trees.iterator();
					JavaClassTree currentTree;
					while (it.hasNext()) {
						currentTree = it.next();
						if (currentTree.contains(this.getDepend(relationItem))) {
							if (currentTree.getJavaClassRoots().contains(this.getDepend(relationItem))) {
								this.appendTree(it, tree, currentTree, currentClass);
							} else {
								this.mergeTree(it, tree, currentTree, currentClass, this.getDepend(relationItem));
							}
						}
					}
				}
			}
		}
	}

	protected void appendTree(Iterator<JavaClassTree> it, JavaClassTree tree, JavaClassTree currentTree,
			JavaClass currentClass) {
		tree.appendTree(currentClass, currentTree);
		it.remove();
	}

	protected void mergeTree(Iterator<JavaClassTree> it, JavaClassTree tree, JavaClassTree currentTree,
			JavaClass currentClass, JavaClass dependJavaClass) {
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
