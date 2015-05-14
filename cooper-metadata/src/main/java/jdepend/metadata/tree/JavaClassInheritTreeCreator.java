package jdepend.metadata.tree;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.metadata.relationtype.InheritRelation;
import jdepend.metadata.relationtype.JavaClassRelationTypeMgr;

public class JavaClassInheritTreeCreator extends JavaClassTreeCreator {

	public JavaClassInheritTreeCreator() {
		super(JavaClassRelationTypeMgr.getInstance().getInheritRelation());
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
}
