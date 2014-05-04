package jdepend.model.tree;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.relationtype.InheritRelation;
import jdepend.model.relationtype.JavaClassRelationTypeMgr;

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

		for (JavaClassRelationItem relationItem : javaClass.getCaItems()) {
			if (relationItem.getType() instanceof InheritRelation) {
				relationTypeItems.add(relationItem);
			}
		}

		return relationTypeItems;
	}
}
