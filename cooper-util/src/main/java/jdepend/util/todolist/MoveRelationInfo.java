package jdepend.util.todolist;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.model.Component;
import jdepend.model.JavaClassUnit;
import jdepend.model.JavaClassRelationItem;

public class MoveRelationInfo {
	
	private Component targetComponent;

	private boolean isChangeDir;

	private Component from;

	private Component fromOther;

	public MoveRelationInfo(Component from, Component fromOther) {
		this.from = from;
		this.fromOther = fromOther;
	}

	public Component getTargetComponent() {
		return targetComponent;
	}

	public void setTargetComponent(Component targetComponent) {
		this.targetComponent = targetComponent;
	}

	public boolean isChangeDir() {
		return isChangeDir;
	}

	public void setChangeDir(boolean isChangeDir) {
		this.isChangeDir = isChangeDir;
	}

	public Collection<JavaClassUnit> getMoveClasses() {
		return this.from.getClasses();
	}

	public Collection<JavaClassRelationItem> getFromClassRelations() {
		Collection<JavaClassRelationItem> items = new ArrayList<JavaClassRelationItem>();

		for (JavaClassRelationItem item : this.from.caCouplingDetail(this.fromOther).getItems()) {
			items.add(item);
		}
		for (JavaClassRelationItem item : this.from.ceCouplingDetail(this.fromOther).getItems()) {
			items.add(item);
		}
		return items;
	}

}
