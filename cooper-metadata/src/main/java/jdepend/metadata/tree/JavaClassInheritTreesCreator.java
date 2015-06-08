package jdepend.metadata.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.metadata.relationtype.InheritRelation;

public class JavaClassInheritTreesCreator extends JavaClassTreesCreator {

	public JavaClassInheritTreesCreator() {
		super();
	}

	/**
	 * 得到指定JavaClassRelationType的关系信息集合
	 * 
	 * @param javaClass
	 * @return
	 */
	@Override
	protected Collection<JavaClassRelationItem> getRelationItem(JavaClass javaClass) {

		Collection<JavaClassRelationItem> relationTypeItems = new ArrayList<JavaClassRelationItem>();

		for (JavaClassRelationItem relationItem : javaClass.getSelfCaItems()) {
			if (relationItem.getType() instanceof InheritRelation) {
				relationTypeItems.add(relationItem);
			}
		}

		return relationTypeItems;
	}

	@Override
	protected JavaClass getDepend(JavaClassRelationItem item) {
		return item.getSource();
	}

	@Override
	protected JavaClass getCurrent(JavaClassRelationItem item) {
		return item.getTarget();
	}

	@Override
	protected void mergeTree(Iterator<JavaClassTree> it, JavaClassTree tree, JavaClassTree currentTree,
			JavaClass currentClass, JavaClass dependJavaClass) {
		tree.mergeTree(currentTree, currentClass, dependJavaClass);
		it.remove();
	}

}
