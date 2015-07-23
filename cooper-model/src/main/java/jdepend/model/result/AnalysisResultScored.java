package jdepend.model.result;

import jdepend.framework.util.MetricsFormat;
import jdepend.model.Scored;

public abstract class AnalysisResultScored implements Scored {

	private transient Float distance = null;// 抽象程度合理性
	private transient Float balance = null;// 内聚性
	private transient Float encapsulation = null;// 组件封装性
	private transient Float relationRationality = null;// 关系合理性

	private transient boolean distanceCal = false;// 抽象程度合理性
	private transient boolean balanceCal = false;// 内聚性
	private transient boolean encapsulationCal = false;// 组件封装性
	private transient boolean relationRationalityCal = false;// 关系合理性

	/**
	 * 得到抽象程度合理性得分
	 * 
	 * @return
	 */
	@Override
	public synchronized Float getDistance() {
		if (!this.distanceCal) {
			Float distanceSummary = getSummary().getDistance();
			if (distanceSummary == null) {
				this.distance = null;
			} else {
				this.distance = MetricsFormat.toFormattedScore(distanceSummary * this.getDistanceScale());
			}
			this.distanceCal = true;
		}
		return this.distance;
	}

	/**
	 * 得到内聚性指数得分
	 * 
	 * @return
	 */
	@Override
	public synchronized Float getBalance() {
		if (!this.balanceCal) {
			Float balanceSummary = getSummary().getBalance();
			if (balanceSummary == null) {
				balance = null;
			} else {
				balance = MetricsFormat.toFormattedScore(balanceSummary * this.getBalanceScale());
			}
			this.balanceCal = true;
		}
		return this.balance;
	}

	/**
	 * 封装性得分
	 * 
	 * @return
	 */
	@Override
	public synchronized Float getEncapsulation() {
		if (!this.encapsulationCal) {
			Float encapsulationSummary = getSummary().getEncapsulation();
			if (encapsulationSummary == null) {
				this.encapsulation = null;
			} else {
				this.encapsulation = MetricsFormat.toFormattedMetrics(encapsulationSummary
						* this.getEncapsulationScale());
			}
			this.encapsulationCal = true;
		}
		return this.encapsulation;
	}

	/**
	 * 得到关系合理性得分
	 * 
	 * @return
	 */
	@Override
	public synchronized Float getRelationRationality() {
		// 关系合理性得分=没有问题的关系比例权值
		if (!this.relationRationalityCal) {
			if (!this.hasRelation()) {
				this.relationRationality = null;
			} else {
				this.relationRationality = MetricsFormat.toFormattedScore((1 - this.calAttentionRelation())
						* this.getRelationRationalityScale());
			}
			this.relationRationalityCal = true;
		}
		return this.relationRationality;
	}

	@Override
	public Float getScore() {
		Float distance = getDistance();
		Float balance = getBalance();
		Float relationRationality = getRelationRationality();
		Float encapsulation = getEncapsulation();
		if (distance != null && balance != null && relationRationality != null && encapsulation != null) {
			return MetricsFormat.toFormattedScore(distance + balance + relationRationality + encapsulation);
		} else {
			return null;
		}
	}

	public synchronized void clearScore() {
		this.distance = null;
		this.balance = null;
		this.relationRationality = null;
		this.encapsulation = null;

		this.distanceCal = false;
		this.balanceCal = false;
		this.relationRationalityCal = false;
		this.encapsulationCal = false;
	}

	public abstract AnalysisResultSummary getSummary();

	protected abstract boolean hasRelation();

	protected abstract float calAttentionRelation();

	public abstract float getDistanceScale();

	public abstract float getBalanceScale();

	public abstract float getEncapsulationScale();

	public abstract float getRelationRationalityScale();

	public float getFullScore() {
		return this.getDistanceScale() + this.getBalanceScale() + this.getEncapsulationScale()
				+ this.getRelationRationalityScale();
	}

}
