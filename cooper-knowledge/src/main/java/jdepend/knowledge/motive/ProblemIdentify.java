package jdepend.knowledge.motive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jdepend.framework.util.MetricsFormat;
import jdepend.knowledge.motive.problem.ComponentProblem;
import jdepend.knowledge.motive.problem.RelationProblem;
import jdepend.knowledge.util.CohesionUtil;
import jdepend.model.Component;
import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;

public class ProblemIdentify {

	private static final Float RelationProblemOrder = 10000F;
	private static final Float HCouplingLCohesionProblemOrder = 1000F;

	private List<Problem> problems = new ArrayList<Problem>();

	public Collection<Problem> identify(AnalysisResult result) {

		this.identifyRelationProblem(result);

		this.identifyHCouplingLCohesionProblem(result);

		// 按Order排序
		Collections.sort(problems);

		return problems;
	}

	private void identifyRelationProblem(AnalysisResult result) {

		Collection<Relation> relations = result.getRelations();
		Float attentionLevel = null;
		RelationProblem relationProblem;
		Relation otherRelation;
		for (Relation relation : relations) {
			if (relation.isAttention()) {
				attentionLevel = relation.getAttentionLevel();
				if (relation.getAttentionType() == Relation.MutualDependAttentionType) {
					// 搜索彼此依赖的“对方”关系
					otherRelation = result.getTheRelation(relation.getDepend().getName(), relation.getCurrent()
							.getName());
					if (attentionLevel > otherRelation.getAttentionLevel()) {
						relationProblem = new RelationProblem(relation);
						relationProblem.setType(Problem.MutualDependRelationProblem);
						relationProblem.setOrder(RelationProblemOrder + attentionLevel);
						// 寻找循环依赖中“细”的那条依赖关系
						if (attentionLevel - Relation.MutualDependAttentionType > 0.8) {
							relationProblem.setName(relation.getCurrent().getName() + " 与 "
									+ relation.getDepend().getName() + " 存在彼此依赖，并且前者对后者的依赖较少");
						} else {
							relationProblem.setName(relation.getCurrent().getName() + " 与 "
									+ relation.getDepend().getName() + " 存在彼此依赖");
						}
						this.problems.add(relationProblem);
					}
				} else if (relation.getAttentionType() == Relation.CycleDependAttentionType) {
					relationProblem = new RelationProblem(relation);
					relationProblem.setName(relation.getCurrent().getName() + " 与 " + relation.getDepend().getName()
							+ " 在循环依赖链上");
					relationProblem.setType(Problem.CycleDependRelationProblem);
					relationProblem.setOrder(RelationProblemOrder + attentionLevel);
					this.problems.add(relationProblem);
				} else if (relation.getAttentionType() == Relation.ComponentLayerAttentionType) {
					relationProblem = new RelationProblem(relation);
					relationProblem.setName(relation.getCurrent().getName() + " 依赖了 " + relation.getDepend().getName()
							+ "(下层组件依赖了上层组件)");
					relationProblem.setType(Problem.ComponentLayerRelationProblem);
					relationProblem.setOrder(RelationProblemOrder + attentionLevel);
					this.problems.add(relationProblem);
				} else if (relation.getAttentionType() == Relation.SDPAttentionType) {
					if (attentionLevel - Relation.SDPAttentionType > 0.1) {
						relationProblem = new RelationProblem(relation);
						relationProblem.setName(relation.getCurrent().getName() + " 依赖了 "
								+ relation.getDepend().getName() + "(稳定组件依赖了不稳定组件)");
						relationProblem.setType(Problem.SDPRelationProblem);
						relationProblem.setOrder(RelationProblemOrder + attentionLevel);
						this.problems.add(relationProblem);
					}
				}
			}
		}
	}

	private void identifyHCouplingLCohesionProblem(AnalysisResult result) {
		ComponentProblem componentProblem;
		for (Component component : CohesionUtil.sort(result)) {
			if (component.balance() < 0.5) {
				componentProblem = new ComponentProblem(component);
				componentProblem.setName(component.getName() + " 组件内聚性为[ "
						+ MetricsFormat.toFormattedMetrics(component.balance()) + "]");
				componentProblem.setType(Problem.HCouplingLCohesionProblem);
				componentProblem.setOrder(HCouplingLCohesionProblemOrder + (1F - component.balance()));
				this.problems.add(componentProblem);
			}

		}
	}

}
