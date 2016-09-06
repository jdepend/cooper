package jdepend.metadata.relationtype;

import java.util.Collection;
import java.util.HashSet;

import jdepend.metadata.HttpInvokeItem;
import jdepend.metadata.InvokeItem;
import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaClassDetail;
import jdepend.metadata.Method;
import jdepend.metadata.util.JavaClassCollection;

public class HttpRelation extends BaseJavaClassRelationType {

	private static final long serialVersionUID = 8142224457447489950L;

	public HttpRelation(float intensity) {
		super(JavaClassRelationTypes.Http, intensity);
	}

	@Override
	public boolean canAbstraction() {
		return false;
	}

	@Override
	public boolean invokeRelated() {
		return true;
	}

	@Override
	public boolean create(JavaClass javaClass, JavaClassCollection javaClasses) {
		boolean isCreate = false;
		JavaClassDetail info = javaClass.getDetail();
		if (info.isHttpCaller()) {
			// 收集Http调用的类集合
			Collection<JavaClass> dependClasses = new HashSet<JavaClass>();
			for (Method method : javaClass.getSelfMethods()) {
				for (InvokeItem invokeItem : method.getInvokeItems()) {
					if (invokeItem instanceof HttpInvokeItem) {
						if (!dependClasses.contains(invokeItem.getCallee().getJavaClass())) {
							dependClasses.add(invokeItem.getCallee().getJavaClass());
						}
					}
				}
			}
			// 建立Http类关系
			for (JavaClass dependClass : dependClasses) {
				if (setDependInfo(javaClass, dependClass, this)) {
					isCreate = true;
				}
			}
		}
		return isCreate;
	}
}
