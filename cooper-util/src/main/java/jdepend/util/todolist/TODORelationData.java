package jdepend.util.todolist;

import jdepend.model.Relation;
import jdepend.model.RelationData;

public class TODORelationData extends RelationData {

	private boolean inited = false;

	private boolean appendRelations = false;

	private boolean todo = false;

	public TODORelationData(Relation relation) {
		super(relation);
	}

	@Override
	public void init() {
		if (!inited) {
			super.init();
			this.inited = true;
		}
	}

	@Override
	public void appendRelations() {
		if (!this.appendRelations) {
			super.appendRelations();
			this.appendRelations = true;
		}
	}

	public boolean isTodo() {
		return todo;
	}

	public void setTodo(boolean todo) {
		this.todo = todo;
	}

}
