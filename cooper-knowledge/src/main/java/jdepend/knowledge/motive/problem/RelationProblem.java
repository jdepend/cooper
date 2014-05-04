package jdepend.knowledge.motive.problem;

import java.io.IOException;
import java.io.ObjectInputStream;

import jdepend.framework.exception.JDependException;
import jdepend.knowledge.motive.Problem;
import jdepend.knowledge.motive.Reason;
import jdepend.model.JDependUnitMgr;
import jdepend.model.Relation;

public final class RelationProblem extends Problem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7027208373557579318L;

	private String current;
	private String depend;
	
	private transient Relation relation;

	public RelationProblem(Relation relation) {
		super();
		this.relation = relation;
		this.current = this.relation.getCurrent().getName();
		this.depend = this.relation.getDepend().getName();
	}

	public Relation getRelation() {
		return relation;
	}

	public String getCurrent() {
		return current;
	}

	public String getDepend() {
		return depend;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	@Override
	public void doExecute() throws JDependException {
		if (this.isImportant() != null && !this.isImportant()) {
			if (this.relation != null) {
				if (this.relation.getAttentionType() == Relation.MutualDependAttentionType) {
					Relation otherRelation = JDependUnitMgr.getInstance().getResult().getTheRelation(depend, current);
					if (otherRelation != null) {
						otherRelation.setAttention(false);
					}
				}
				this.relation.setAttention(false);
			}
		}
	}

	@Override
	protected void doCheck() throws JDependException {
		if (this.getReason() != null) {
			if (this.getReason().getName().equals(Reason.TheSameAreaReason)) {
				if (!this.getType().equals(Problem.MutualDependRelationProblem)
						&& !this.getType().equals(Problem.CycleDependRelationProblem)) {
					throw new JDependException("原因对应的问题类型应该是彼此依赖或循环依赖");
				}
				if (this.relation.getCurrent().getComponent().getAreaComponent() == null) {
					throw new JDependException("组件[" + this.current + "]未指定区域");
				}
				if (this.relation.getDepend().getComponent().getAreaComponent() == null) {
					throw new JDependException("组件[" + this.depend + "]未指定区域");
				}
				if (!this.relation.getCurrent().getComponent().getAreaComponent().equals(
						this.relation.getDepend().getComponent().getAreaComponent())) {
					throw new JDependException("组件[" + this.current + "]和组件[" + this.depend + "]不在一个区域内");
				}
			} else if (this.getReason().getName().equals(Reason.MutabilityToStableReason)) {
				if (!this.getType().equals(Problem.SDPRelationProblem)) {
					throw new JDependException("原因对应的问题类型应该是稳定组件依赖了不稳定组件");
				}
			} else if (this.getReason().getName().equals(Reason.StableToMutabilityReason)) {
				if (!this.getType().equals(Problem.SDPRelationProblem)) {
					throw new JDependException("原因对应的问题类型应该是稳定组件依赖了不稳定组件");
				}
			}
		}
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		this.relation = JDependUnitMgr.getInstance().getResult().getTheRelation(current, depend);
	}
}
