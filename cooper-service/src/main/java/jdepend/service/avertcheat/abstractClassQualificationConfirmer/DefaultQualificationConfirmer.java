package jdepend.service.avertcheat.abstractClassQualificationConfirmer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.model.JavaClass;

public class DefaultQualificationConfirmer implements QualificationConfirmer {

	protected static int ChildJavaClassCount = 2;

	public DefaultQualificationConfirmer() {
	}

	public boolean confirmAbstractClassCount(JavaClass javaClass) {
		if (!javaClass.isAbstract())
			return false;

		for (String javaClasses : getConfirmJavaClass()) {
			if (javaClass.getName().equals(javaClasses)) {
				return true;
			}
		}

		Collection<JavaClass> subClasses = javaClass.getSubClasses();
		if (subClasses.size() >= ChildJavaClassCount) {
			return true;
		} else {
			// 子类不在一个组件中也具备抽象类计数资格
			for (JavaClass subClass : subClasses) {
				if (subClass.getComponent() != null && javaClass.getComponent() != null
						&& !subClass.getComponent().equals(javaClass.getComponent())) {
					return true;
				}
			}
			// 存在一个子类，又存在父类也具备抽象类计数资格
			if (subClasses.size() >= 1 && javaClass.getSupers().size() > 0) {
				return true;
			}
			return false;
		}
	}

	protected List<String> getConfirmJavaClass() {
		List<String> javaClasses = new ArrayList<String>();
		return javaClasses;
	}
}
