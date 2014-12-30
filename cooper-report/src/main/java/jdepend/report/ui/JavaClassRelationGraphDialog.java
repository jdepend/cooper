package jdepend.report.ui;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.framework.ui.CooperDialog;
import jdepend.framework.ui.JDependFrame;
import jdepend.model.Component;
import jdepend.model.JavaClass;
import jdepend.model.Relation;
import jdepend.model.component.JavaClassComponent;
import jdepend.model.util.RelationCreator;
import jdepend.report.way.mapui.GraphPanel;

public class JavaClassRelationGraphDialog extends CooperDialog {

	public JavaClassRelationGraphDialog(JDependFrame frame, JavaClass javaClass) {

		super(javaClass.getName() + "传出|传入");

		Collection<Component> components = new ArrayList<Component>();

		components.add(new JavaClassComponent(javaClass));

		Collection<Component> componentCas = new ArrayList<Component>();

		for (JavaClass relJavaClass : javaClass.getCaList()) {
			componentCas.add(new JavaClassComponent(relJavaClass));
		}

		Collection<Component> componentCes = new ArrayList<Component>();

		for (JavaClass relJavaClass : javaClass.getCeList()) {
			componentCes.add(new JavaClassComponent(relJavaClass));
		}

		Collection<Relation> relations = new ArrayList<Relation>();

		relations.addAll(new RelationCreator().create(componentCas, components));
		relations.addAll(new RelationCreator().create(components, componentCes));

		this.add(new GraphPanel(frame, this, relations));

	}

}
