package jdepend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import jdepend.metadata.InvokeItem;
import jdepend.metadata.Method;
import jdepend.model.component.VirtualComponent;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.RelationCreator;

public class JavaClassWrapper {

	private JavaClassUnit javaClass;

	private transient Collection<JavaClassUnit> invokeClasses;

	public JavaClassWrapper(JavaClassUnit javaClass) {
		super();
		this.javaClass = javaClass;
	}

	public synchronized Collection<JavaClassUnit> getInvokeClasses() {
		if (this.invokeClasses == null) {
			this.invokeClasses = new HashSet<JavaClassUnit>();
			this.collectInvokeClasses(javaClass, invokeClasses);
		}
		return this.invokeClasses;

	}

	public Collection<Relation> getRelations() {

		Collection<Component> components = new ArrayList<Component>();

		VirtualComponent javaClassComponent = new VirtualComponent(javaClass);
		components.add(javaClassComponent);

		Collection<Component> componentCas = new ArrayList<Component>();

		for (JavaClassUnit relJavaClass : javaClass.getCaList()) {
			javaClassComponent = new VirtualComponent(relJavaClass);
			componentCas.add(javaClassComponent);
		}

		Collection<Component> componentCes = new ArrayList<Component>();

		for (JavaClassUnit relJavaClass : javaClass.getCeList()) {
			javaClassComponent = new VirtualComponent(relJavaClass);
			componentCes.add(javaClassComponent);
		}

		Collection<Relation> relations = new ArrayList<Relation>();

		relations.addAll(new RelationCreator().create(componentCas, components));
		relations.addAll(new RelationCreator().create(components, componentCes));

		return relations;
	}

	private void collectInvokeClasses(JavaClassUnit javaClass, Collection<JavaClassUnit> invokeClasses) {

		AnalysisResult result = javaClass.getResult();
		JavaClassUnit invokeClass;
		for (Method method : javaClass.getJavaClass().getSelfMethods()) {
			for (InvokeItem invokeItem : method.getInvokeItems()) {
				invokeClass = result.getTheClass(invokeItem.getCallee().getJavaClass().getId());
				if (!invokeClasses.contains(invokeClass)) {
					invokeClasses.add(invokeClass);
					collectInvokeClasses(invokeClass, invokeClasses);
				}
			}
		}
	}

}
