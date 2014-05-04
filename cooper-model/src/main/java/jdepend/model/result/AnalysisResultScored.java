package jdepend.model.result;

import jdepend.framework.util.MetricsFormat;
import jdepend.model.Scored;

public abstract class AnalysisResultScored implements Scored {

	public static final float FullScore = 100F;// 满分
	public static final float D = 25F;// 抽象程度合理性得分比例
	public static final float Balance = 25F;// 内聚性得分比例
	public static final float Encapsulation = 25F;// 封装性得分比例
	public static final float RelationRationality = 25F;// 关系合理性得分比例

	private transient Float d = null;// 抽象程度合理性
	private transient Float balance = null;// 内聚性
	private transient Float encapsulation = null;// 组件封装性
	private transient Float relationRationality = null;// 关系合理性

	/**
	 * 得到抽象程度合理性得分
	 * 
	 * @return
	 */
	@Override
	public float calD() {
		if (this.d == null) {
			this.d = MetricsFormat.toFormattedScore((1 - getSummary().getDistance()) * D);
		}
		return this.d;
	}

	/**
	 * 得到内聚性指数得分
	 * 
	 * @return
	 */
	@Override
	public float calBalance() {
		if (this.balance == null) {
			balance = MetricsFormat.toFormattedScore(getSummary().getBalance() * Balance);
		}
		return this.balance;
	}

	/**
	 * 封装性得分
	 * 
	 * @return
	 */
	@Override
	public float calEncapsulation() {
		if (this.encapsulation == null) {
			this.encapsulation = MetricsFormat.toFormattedMetrics(getSummary().getEncapsulation() * Encapsulation);
		}
		return this.encapsulation;
	}

	/**
	 * 得到关系合理性得分
	 * 
	 * @return
	 */
	@Override
	public float calRelationRationality() {
		// 关系合理性得分=没有问题的关系比例权值
		if (this.relationRationality == null) {
			if (!this.hasRelation()) {
				this.relationRationality = 30F;
			} else {
				this.relationRationality = MetricsFormat.toFormattedScore((1 - this.calAttentionRelation())
						* RelationRationality);
			}
		}
		return this.relationRationality;
	}

	@Override
	public float calScore() {
		return MetricsFormat.toFormattedScore(calD() + calBalance() + calRelationRationality() + calEncapsulation());
	}

	public void clearScore() {
		this.d = null;
		this.balance = null;
		this.relationRationality = null;
		this.encapsulation = null;
	}

	public abstract AnalysisResultSummary getSummary();

	public abstract boolean hasRelation();

	public abstract float calAttentionRelation();

}
