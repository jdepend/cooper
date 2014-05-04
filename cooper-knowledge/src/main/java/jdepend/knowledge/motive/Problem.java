package jdepend.knowledge.motive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;

public abstract class Problem implements Serializable, Comparable<Problem> {

	private String name;
	private String type;
	private Float order;
	private Boolean isImportant;
	private Reason reason;

	public final static String MutualDependRelationProblem = "彼此依赖";
	public final static String CycleDependRelationProblem = "循环依赖";
	public static final String ComponentLayerRelationProblem = "下层组件依赖了上层组件";
	public static final String SDPRelationProblem = "稳定组件依赖了不稳定组件";

	public final static String HCouplingLCohesionProblem = "高耦合低内聚";

	private final static List<String> types = new ArrayList<String>();

	static {
		types.add(MutualDependRelationProblem);
		types.add(CycleDependRelationProblem);
		types.add(ComponentLayerRelationProblem);
		types.add(SDPRelationProblem);
		types.add(HCouplingLCohesionProblem);
	}

	public static List<String> getTypes() {
		return types;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Float getOrder() {
		return order;
	}

	public void setOrder(Float order) {
		this.order = order;
	}

	public Boolean isImportant() {
		return isImportant;
	}

	public void setImportant(Boolean isImportant) {
		this.isImportant = isImportant;
	}

	public Reason getReason() {
		return reason;
	}

	public void setReason(Reason reason) {
		this.reason = reason;
	}

	public final void execute() throws JDependException {
		this.check();
		this.doExecute();
	}

	public final void check() throws JDependException {
		if (this.isImportant != null && !this.isImportant && this.reason == null) {
			throw new JDependException("不重要的问题需要给出理由");
		}
		doCheck();
	}

	protected void doCheck() throws JDependException {
	}

	protected void doExecute() throws JDependException {

	}

	@Override
	public int compareTo(Problem o) {
		if (this.isImportant == null && o.isImportant == null) {
			return o.order.compareTo(this.order);
		} else if (this.isImportant && (o.isImportant == null || !o.isImportant)) {
			return 1;
		} else if (this.isImportant == null && o.isImportant) {
			return -1;
		} else {
			return o.order.compareTo(this.order);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Problem other = (Problem) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
