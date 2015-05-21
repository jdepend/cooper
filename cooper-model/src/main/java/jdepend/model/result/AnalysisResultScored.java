package jdepend.model.result;

import jdepend.framework.util.MetricsFormat;
import jdepend.model.Scored;

public abstract class AnalysisResultScored implements Scored {

	public static final float FullScore = 100F;// 满分
	public static final float Distance = 25F;// 抽象程度合理性得分比例
	public static final float Balance = 25F;// 内聚性得分比例
	public static final float Encapsulation = 25F;// 封装性得分比例
	public static final float RelationRationality = 25F;// 关系合理性得分比例

	private transient Float distance = null;// 抽象程度合理性
	private transient Float balance = null;// 内聚性
	private transient Float encapsulation = null;// 组件封装性
	private transient Float relationRationality = null;// 关系合理性

	/**
	 * 得到抽象程度合理性得分
	 * 
	 * @return
	 */
	@Override
	public synchronized float getDistance() {
		if (this.distance == null) {
			this.distance = MetricsFormat.toFormattedScore((1 - getSummary().getDistance()) * Distance);
		}
		return this.distance;
	}

	/**
	 * 得到内聚性指数得分
	 * 
	 * @return
	 */
	@Override
	public synchronized float getBalance() {
		if (this.balance == null) {
			Float balanceSummary = getSummary().getBalance();
			if (balanceSummary == null) {
				balanceSummary = 0.5F;
			}
			balance = MetricsFormat.toFormattedScore(balanceSummary * Balance);
		}
		return this.balance;
	}

	/**
	 * 封装性得分
	 * 
	 * @return
	 */
	@Override
	public synchronized float getEncapsulation() {
		if (this.encapsulation == null) {
			Float encapsulationSummary = getSummary().getEncapsulation();
			if (encapsulationSummary == null) {
				encapsulationSummary = 0.5F;
			}
			this.encapsulation = MetricsFormat.toFormattedMetrics(encapsulationSummary * Encapsulation);
		}
		return this.encapsulation;
	}

	/**
	 * 得到关系合理性得分
	 * 
	 * @return
	 */
	@Override
	public synchronized float getRelationRationality() {
		// 关系合理性得分=没有问题的关系比例权值
		if (this.relationRationality == null) {
			if (!this.hasRelation()) {
				this.relationRationality = 25F;
			} else {
				this.relationRationality = MetricsFormat.toFormattedScore((1 - this.calAttentionRelation())
						* RelationRationality);
			}
		}
		return this.relationRationality;
	}

	@Override
	public float getScore() {
		return MetricsFormat.toFormattedScore(getDistance() + getBalance() + getRelationRationality()
				+ getEncapsulation());
	}

	public synchronized void clearScore() {
		this.distance = null;
		this.balance = null;
		this.relationRationality = null;
		this.encapsulation = null;
	}

	public abstract AnalysisResultSummary getSummary();

	public abstract boolean hasRelation();

	public abstract float calAttentionRelation();

}
