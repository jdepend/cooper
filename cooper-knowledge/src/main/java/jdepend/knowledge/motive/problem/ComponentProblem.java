package jdepend.knowledge.motive.problem;

import java.io.IOException;
import java.io.ObjectInputStream;

import jdepend.knowledge.motive.Problem;
import jdepend.model.Component;
import jdepend.model.JDependUnitMgr;

public final class ComponentProblem extends Problem {

	private static final long serialVersionUID = 6909797135208179650L;

	private String componentName;

	private transient Component component;

	public ComponentProblem(Component component) {
		super();
		this.component = component;
		this.componentName = this.component.getName();
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		this.component = JDependUnitMgr.getInstance().getResult().getTheComponent(this.componentName);
	}

}
