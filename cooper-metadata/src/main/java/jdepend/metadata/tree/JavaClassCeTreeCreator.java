package jdepend.metadata.tree;

import java.util.Collection;
import java.util.HashSet;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.metadata.relationtype.InheritRelation;

public class JavaClassCeTreeCreator extends JavaClassTreeCreator {

	public JavaClassCeTreeCreator() {
		super();
	}

	@Override
	protected Collection<JavaClass> getRelationClass(JavaClass javaClass) {

		Collection<JavaClass> javaClasses = new HashSet<JavaClass>();

		for (JavaClassRelationItem relationItem : javaClass.getCeItems()) {
			if (!(relationItem.getType() instanceof InheritRelation)) {
				javaClasses.add(relationItem.getTarget());
			}
		}

		return javaClasses;
	}
}
