package jdepend.metadata.tree;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.metadata.relationtype.InheritRelation;

public class JavaClassCaTreeCreator extends JavaClassTreeCreator {

	public JavaClassCaTreeCreator() {
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

		for (JavaClassRelationItem relationItem : javaClass.getCaItems()) {
			if (!(relationItem.getType() instanceof InheritRelation)) {
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
