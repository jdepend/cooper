package jdepend.model;

public abstract class AbstractSubJDependUnit extends AbstractJDependUnit implements SubJDependUnit {

	private transient GroupCohesionInfo groupCohesionInfo = null;

	private transient GroupCouplingInfo groupCouplingInfo = null;

	private transient GroupInfoCalculator groupInfoCalculator;

	public AbstractSubJDependUnit() {
		super();
	}

	public AbstractSubJDependUnit(String name) {
		super(name);
	}

	public synchronized GroupCouplingInfo getGroupCouplingInfo() {
		if (this.groupCouplingInfo == null) {
			this.groupCouplingInfo = this.getGroupInfoCalculator().getGroupCouplingInfo();
		}
		return this.groupCouplingInfo;
	}

	@Override
	public synchronized GroupCohesionInfo getGroupCohesionInfo() {
		if (this.groupCohesionInfo == null) {
			this.groupCohesionInfo = this.getGroupInfoCalculator().getGroupCohesionInfo();
		}
		return this.groupCohesionInfo;
	}

	protected synchronized GroupInfoCalculator getGroupInfoCalculator() {
		if (this.groupInfoCalculator == null) {
			this.groupInfoCalculator = createGroupInfoCalculator();
		}
		return this.groupInfoCalculator;
	}

	protected abstract GroupInfoCalculator createGroupInfoCalculator();

	@Override
	public void clear() {
		super.clear();

		this.groupCohesionInfo = null;
		this.groupCouplingInfo = null;
		this.groupInfoCalculator = null;
	}

}
