package jdepend.model.result;

import java.util.Collection;

import jdepend.framework.util.MetricsFormat;
import jdepend.metadata.JavaClassRelationItem;
import jdepend.metadata.relationtype.TableRelation;
import jdepend.model.JavaClassUnit;
import jdepend.model.Relation;
import jdepend.model.util.JavaClassUnitUtil;

public class AnalysisResultUtil {

	private AnalysisResult result;

	public AnalysisResultUtil(AnalysisResult result) {
		super();
		this.result = result;
	}

	/**
	 * 计算基于数据库表实现组件间通讯的比重
	 * 
	 * @param javaClasses
	 * @return
	 */
	public Float tableRelationScale() {

		Collection<JavaClassUnit> javaClasses = result.getClasses();

		if (javaClasses != null && javaClasses.size() > 0) {
			AnalysisResult result = javaClasses.iterator().next().getResult();
			int tableRelations = 0;
			int relations = 0;
			for (JavaClassUnit javaClass : javaClasses) {
				for (JavaClassRelationItem item : javaClass.getJavaClass().getCeItems()) {
					if (!JavaClassUnitUtil.isInner(item, result)) {
						if (item.getType() instanceof TableRelation) {
							tableRelations += 1;
						}
						relations += 1;
					}
				}
			}
			if (relations == 0) {
				return 0.0F;
			} else {
				return MetricsFormat.toFormattedMetrics(tableRelations * 1F / relations);
			}
		} else {
			return 0.0F;
		}
	}

	/**
	 * 计算存在问题的关系的权重
	 * 
	 * @return
	 */
	protected float calAttentionRelation() {

		float scale = 0F;

		if (result.getRelations().size() == 0) {
			scale = 0F;
		} else {
			float attentions = 0F;
			for (Relation relation : result.getRelations()) {
				if (relation.isAttention()) {
					// 根据关系性质计算存在问题的关系比例
					if (relation.getAttentionType() == Relation.MutualDependAttentionType) {
						attentions += 1.0F;// 彼此依赖一次增加1，两次增加到2（存在彼此依赖的关系，两条线全部记录为有问题的关系）
					} else if (relation.getAttentionType() == Relation.ComponentLayerAttentionType) {
						attentions += 0.8F;
					} else if (relation.getAttentionType() == Relation.SDPAttentionType) {
						attentions += 0.5F;
					}
					if (relation.getAttentionType() == Relation.CycleDependAttentionType) {
						attentions += 0.3F;
					}
				}
			}
			scale = attentions * 1F / result.getRelations().size();
		}

		return scale;
	}

	/**
	 * 计算存在问题的关系的比例
	 * 
	 * @return
	 */
	public float getAttentionRelationScale() {

		float scale = 0F;

		if (result.getRelations().size() == 0) {
			scale = 0F;
		} else {
			int isAttention = 0;
			for (Relation relation : result.getRelations()) {
				if (relation.isAttention()) {
					isAttention++;
				}
			}
			scale = isAttention * 1F / result.getRelations().size();
		}

		return scale;
	}

	/**
	 * 计算平均类大小
	 * 
	 * @return
	 */
	public int calClassSize() {
		int classCount = 0;
		for (JavaClassUnit JavaClass : result.getClasses()) {
			if (JavaClass.getLineCount() != 0) {
				classCount += 1;
			}
		}
		if (classCount != 0) {
			return result.getSummary().getLineCount() / classCount;
		} else {
			return 0;
		}
	}

}
