package jdepend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import jdepend.model.component.JavaClassComponent;
import jdepend.model.component.VirtualComponent;
import jdepend.model.util.RelationCreator;

public class JavaClassWrapper {

	private JavaClass javaClass;

	private transient Collection<JavaClass> invokeClasses;

	public JavaClassWrapper(JavaClass javaClass) {
		super();
		this.javaClass = javaClass;
	}

	public synchronized Collection<JavaClass> getInvokeClasses() {
		if (this.invokeClasses == null) {
			this.invokeClasses = new HashSet<JavaClass>();
			this.collectInvokeClasses(javaClass, invokeClasses);
		}
		return this.invokeClasses;

	}

	public Collection<Relation> getRelations() {

		Collection<Component> components = new ArrayList<Component>();

		VirtualComponent javaClassComponent = new VirtualComponent(javaClass);
		components.add(javaClassComponent);

		Collection<Component> componentCas = new ArrayList<Component>();

		for (JavaClass relJavaClass : javaClass.getCaList()) {
			javaClassComponent = new VirtualComponent(relJavaClass);
			componentCas.add(javaClassComponent);
		}

		Collection<Component> componentCes = new ArrayList<Component>();

		for (JavaClass relJavaClass : javaClass.getCeList()) {
			javaClassComponent = new VirtualComponent(relJavaClass);
			componentCes.add(javaClassComponent);
		}

		Collection<Relation> relations = new ArrayList<Relation>();

		relations.addAll(new RelationCreator().create(componentCas, components));
		relations.addAll(new RelationCreator().create(components, componentCes));

		return relations;
	}

	private void collectInvokeClasses(JavaClass javaClass, Collection<JavaClass> invokeClasses) {
		JavaClass invokeClass;
		for (Method method : javaClass.getSelfMethods()) {
			for (InvokeItem invokeItem : method.getInvokeItems()) {
				invokeClass = invokeItem.getCallee().getJavaClass();
				if (!invokeClasses.contains(invokeClass)) {
					invokeClasses.add(invokeClass);
					collectInvokeClasses(invokeClass, invokeClasses);
				}
			}
		}
	}

}
