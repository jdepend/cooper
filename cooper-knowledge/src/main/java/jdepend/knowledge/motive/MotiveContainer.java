package jdepend.knowledge.motive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.domain.PersistentBean;
import jdepend.framework.exception.JDependException;
import jdepend.knowledge.motive.problem.RelationProblem;
import jdepend.model.AreaComponent;
import jdepend.model.Component;
import jdepend.model.Relation;
import jdepend.model.result.AnalysisResult;
import jdepend.model.result.AnalysisResultListener;

public class MotiveContainer extends PersistentBean implements AnalysisResultListener {

	private static final long serialVersionUID = 5669041380968138345L;

	private String group;

	private String command;

	private Collection<Motive> motives;

	private Collection<MotiveOperation> operations;

	private Collection<Problem> problems;

	private AdjustInfo adjustInfo;

	private transient boolean needRefresh = false;

	private transient MemoryOperation memoryOperation = new MemoryOperation();

	private transient Map<String, Problem> problemForNames;

	public MotiveContainer(String group, String command) {
		super("设计动机信息包", "设计动机信息包", PropertyConfigurator.DEFAULT_PROPERTY_DIR + "\\" + group + "\\" + command);

		if (this.group == null) {
			this.group = group;
		}
		if (this.command == null) {
			this.command = command;
		}
		if (operations == null) {
			operations = new ArrayList<MotiveOperation>();
		}
		if (adjustInfo == null) {
			adjustInfo = new AdjustInfo();
		}
		if(motives == null){
			motives = new ArrayList<Motive>();
		}
	}

	public void createInfo(AnalysisResult result) {
		this.problems = new ProblemIdentify().identify(result);
		this.adjustInfo = AdjustInfo.create(result);

		calProblemForNames();
		this.memoryOperation.reset();
	}

	public void reCreateInfo(AnalysisResult result) {
		Collection<Problem> oldProblems = this.problems;
		this.problems = new ProblemIdentify().identify(result);
		if (oldProblems != null) {
			boolean repeat;
			for (Problem oldProblem : oldProblems) {
				if (oldProblem.getReason() != null) {
					repeat = false;
					L: for (Problem problem : this.problems) {
						if (problem.equals(oldProblem)) {
							problem.setImportant(oldProblem.isImportant());
							problem.setReason(oldProblem.getReason());
							repeat = true;
							break L;
						}
					}
					if (!repeat) {
						this.problems.add(oldProblem);
					}
				}
			}
		}
		this.refresh(result);

		calProblemForNames();
		memoryOperation.reset();
	}

	public void reBuild(AnalysisResult result) {
		this.clear(result);
		this.createInfo(result);
	}

	public void execute() throws JDependException {
		for (Problem problem : this.problems) {
			problem.execute();
		}
	}

	public void check() throws JDependException {
		for (Problem problem : this.problems) {
			problem.check();
		}
	}

	@Override
	public void save() throws IOException {
		this.operations.addAll(this.memoryOperation.create());
		this.motives = new MotiveCreator(this).create();
		super.save();
	}

	private void calProblemForNames() {
		this.problemForNames = new HashMap<String, Problem>();
		for (Problem problem : this.problems) {
			this.problemForNames.put(problem.getName(), problem);
		}
	}

	private void clear(AnalysisResult result) {
		for (Relation relation : result.getRelations()) {
			relation.setAttention(true);
			relation.clear();
		}
		for (jdepend.model.Component component : result.getComponents()) {
			component.setAreaComponent(null);
			component.setSteadyType(null);
		}
		result.clearScore();

		this.operations = new ArrayList<MotiveOperation>();
		this.motives = new ArrayList<Motive>();
	}

	private void refresh(AnalysisResult result) {
		if (this.needRefresh) {
			// 设置新的relation
			this.updateProblem(result);
			// 设置新的component
			this.adjustInfo.updateAreaComponent(result);
			// 设置新的Stable
			this.adjustInfo.updateStable(result);

			this.needRefresh = false;
		}
	}

	private void updateProblem(AnalysisResult result) {
		RelationProblem relationProblem = null;
		Relation relation = null;
		Problem problem = null;
		Iterator<Problem> it = problems.iterator();
		while (it.hasNext()) {
			problem = it.next();
			if (problem instanceof RelationProblem) {
				relationProblem = (RelationProblem) problem;
				relation = result.getTheRelation(relationProblem.getCurrent(), relationProblem.getDepend());
				if (relation != null) {
					relationProblem.setRelation(relation);
				} else {
					it.remove();
				}
			}
		}
	}

	public Collection<Motive> getMotives() {
		return motives;
	}

	public Collection<MotiveOperation> getOperations() {
		return operations;
	}

	public Collection<Problem> getProblems() {
		return problems;
	}

	public void updateProblemImportant(String problemName, Boolean isImportant) {
		this.problemForNames.get(problemName).setImportant(isImportant);
		this.memoryOperation.addMemoryOperationP(problemName, MemoryOperation.isImportant, isImportant == null ? ""
				: isImportant ? "重要" : "不重要");
	}

	public void updateProblemReason(String problemName, Reason reason) throws JDependException {
		this.problemForNames.get(problemName).setReason(reason);
		this.problemForNames.get(problemName).check();
		this.memoryOperation.addMemoryOperationP(problemName, MemoryOperation.reason, reason.getDesc() == null ? ""
				: reason.getName());
	}

	public void addMutability(String componentName, Float balance) {
		this.adjustInfo.addMutability(componentName, balance);
		this.memoryOperation.addMemoryOperationS(componentName, MemoryOperation.Middle, MemoryOperation.Mutability);
	}

	public void deleteMutability(String componentName) {
		this.adjustInfo.deleteMutability(componentName);
		this.memoryOperation.addMemoryOperationS(componentName, MemoryOperation.Mutability, MemoryOperation.Middle);
	}

	public void addStable(String componentName, Float balance) {
		this.adjustInfo.addStable(componentName, balance);
		this.memoryOperation.addMemoryOperationS(componentName, MemoryOperation.Middle, MemoryOperation.Stable);
	}

	public void deleteStable(String componentName) {
		this.adjustInfo.deleteStable(componentName);
		this.memoryOperation.addMemoryOperationS(componentName, MemoryOperation.Stable, MemoryOperation.Middle);
	}

	public void deleteMiddle(String componentName) {
		this.adjustInfo.deleteMiddle(componentName);
	}

	public void addMiddle(String componentName, Float balance) {
		this.adjustInfo.addMiddle(componentName, balance);
	}

	public Map<String, Float> getMutabilitys() {
		return this.adjustInfo.getMutabilitys();
	}

	public Map<String, Float> getMiddles() {
		return this.adjustInfo.getMiddles();
	}

	public Map<String, Float> getStables() {
		return this.adjustInfo.getStables();
	}

	public List<AreaComponent> getAreas() {
		return this.adjustInfo.getAreas();
	}

	public void deleteAreaComponent(String areaName) {
		this.adjustInfo.deleteAreaComponent(areaName);
		this.memoryOperation.addMemoryOperationA(areaName, null, MemoryOperation.Delete);
	}

	public void deleteComponent(String areaName, String componentName) throws JDependException {
		this.adjustInfo.deleteComponent(areaName, componentName);
		this.memoryOperation.addMemoryOperationA(areaName, componentName, MemoryOperation.Delete);
	}

	public void addComponent(String areaName, Component component) throws JDependException {
		this.adjustInfo.addComponent(areaName, component);
		this.memoryOperation.addMemoryOperationA(areaName, component.getName(), MemoryOperation.Add);
	}

	public void addAreaComponent(int areaLayer, String areaName, Collection<Component> components) throws JDependException {
		this.adjustInfo.addAreaComponent(areaLayer, areaName, components);
		this.memoryOperation.addMemoryOperationA(areaName, null, MemoryOperation.Add);
		for (Component component : components) {
			this.memoryOperation.addMemoryOperationA(areaName, component.getName(), MemoryOperation.Add);
		}
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public AdjustInfo getAdjustInfo() {
		return adjustInfo;
	}

	public void setAdjustInfo(AdjustInfo adjustInfo) {
		this.adjustInfo = adjustInfo;
	}

	public void setMotives(Collection<Motive> motives) {
		this.motives = motives;
	}

	public void setOperations(Collection<MotiveOperation> operations) {
		this.operations = operations;
	}

	public void setProblems(Collection<Problem> problems) {
		this.problems = problems;
	}

	@Override
	public void onExecuted(AnalysisResult result) throws JDependException {
		this.needRefresh = true;
	}
}
