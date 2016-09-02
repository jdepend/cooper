package jdepend.metadata.tree;

import java.util.Collection;
import java.util.HashSet;

import jdepend.metadata.Attribute;
import jdepend.metadata.JavaClass;
import jdepend.metadata.annotation.Service;

/**
 * 用于创建采用Spring注解生成的类注入树
 *  
 * @author Abner
 *
 */
public class JavaClassAutowiredTreeCreator extends JavaClassTreeCreator {

	public JavaClassAutowiredTreeCreator() {
		super();
	}

	@Override
	protected Collection<JavaClass> getRelationClass(JavaClass javaClass) {

		Collection<JavaClass> javaClasses = new HashSet<JavaClass>();

		for (Attribute attribute : javaClass.getAttributes()) {
			if (attribute.getAutowired() != null || attribute.getQualifier() != null) {
				for (JavaClass attributeClass : attribute.getTypeClasses()) {
					if (attributeClass.isAbstract()) {
						//收集子类
						Collection<JavaClass> subClasses = new HashSet<JavaClass>();
						for (JavaClass subClass : attributeClass.getSubClasses()) {
							if (!subClass.isAbstract()) {
								subClasses.add(subClass);
							}
						}
						if (subClasses.size() == 0) {
							javaClasses.add(attributeClass);//用于识别Spring Data的Repo
						} else if (subClasses.size() == 1) {
							javaClasses.add(subClasses.iterator().next());//用于识别只有一个子类的ServiceImpl
						} else {
							//用于识别有多个实现的子类中指定value的子类
							for (JavaClass subClass : subClasses) {
								Service service = subClass.getDetail().getService();
								if (attribute.getQualifier() != null && attribute.getQualifier().direct(service)) {
									javaClasses.add(subClass);
								}
							}
						}
					} else {
						javaClasses.add(attributeClass);
					}
				}
			}
		}
		return javaClasses;
	}
}
