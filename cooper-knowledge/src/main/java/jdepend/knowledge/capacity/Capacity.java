package jdepend.knowledge.capacity;

import jdepend.framework.util.MetricsFormat;
import jdepend.model.Scored;
import jdepend.model.result.AnalysisResult;

public class Capacity {

	private String group;

	private String command;

	private Complexity complexity;

	private Skill skill;

	private Scored score;

	private String level;

	public static final String HighCapacity = "High";
	public static final String MiddleCapacity = "Middle";
	public static final String LowCapacity = "Low";

	private int resultCode;

	Capacity(AnalysisResult result) {
		super();

		this.group = result.getRunningContext().getGroup();

		this.command = result.getRunningContext().getCommand();

		this.resultCode = result.hashCode();

		complexity = new Complexity(result);

		skill = new Skill(result);

		score = result;

	}

	public String getGroup() {
		return group;
	}

	public String getCommand() {
		return command;
	}

	public String getLevel() {
		if (this.level == null) {
			this.level = this.calLevel();
		}
		return this.level;
	}

	public Complexity getComplexity() {
		return complexity;
	}

	public Skill getSkill() {
		return skill;
	}

	public Scored getScore() {
		return score;
	}

	protected String calLevel() {
		if (score.calScore() >= 80 && complexity.getValue() > 20 && skill.getLevel().equals(Skill.High)) {
			return HighCapacity;
		} else if (score.calScore() < 50) {
			return LowCapacity;
		} else if (score.calScore() < 60 && complexity.getValue() < 80) {
			return LowCapacity;
		} else if (score.calScore() < 70 && score.calScore() >= 60 && complexity.getValue() < 20
				&& skill.getLevel().equals(Skill.Low)) {
			return LowCapacity;
		} else {
			return MiddleCapacity;
		}
	}

	public boolean isSame(AnalysisResult result) {
		return this.resultCode == result.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("Group:");
		info.append(this.group);
		info.append("\n");

		info.append("Command:");
		info.append(this.command);
		info.append("\n");

		info.append("Capacity:");
		info.append(this.getLevel());
		info.append("\n");
		info.append("\n");

		info.append("Complexity:");
		info.append(MetricsFormat.toFormattedMetrics(this.complexity.getValue()));
		info.append("\n");
		info.append("Components:");
		info.append(this.complexity.getComponents());
		info.append("\n");
		info.append("Relations:");
		info.append(this.complexity.getRelations());
		info.append("\n");
		info.append("\n");

		info.append("Skill:\n");
		info.append(this.skill);
		info.append("\n");

		return info.toString();
	}
}
