package jdepend.model.tree;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.relationtype.FieldRelation;
import jdepend.model.relationtype.JavaClassRelationTypeMgr;

public class JavaClassFieldTreeCreator extends JavaClassTreeCreator {

	public JavaClassFieldTreeCreator() {
		super(JavaClassRelationTypeMgr.getInstance().getFieldRelation());
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

		for (JavaClassRelationItem relationItem : javaClass.getSelfCeItems()) {
			if (relationItem.getType() instanceof FieldRelation) {
				relationTypeItems.add(relationItem);
			}
		}

		return relationTypeItems;
	}
}
