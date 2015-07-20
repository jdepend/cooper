package jdepend.model.profile.model;

import java.io.Serializable;

public class AnalysisResultProfile implements Serializable {

	private static final long serialVersionUID = 4340413264825347906L;

	private float distance;

	private float balance;

	private float encapsulation;

	private float relationRationality;

	private boolean componentWeight;

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public float getEncapsulation() {
		return encapsulation;
	}

	public void setEncapsulation(float encapsulation) {
		this.encapsulation = encapsulation;
	}

	public float getRelationRationality() {
		return relationRationality;
	}

	public void setRelationRationality(float relationRationality) {
		this.relationRationality = relationRationality;
	}

	public boolean isComponentWeight() {
		return componentWeight;
	}

	public void setComponentWeight(boolean componentWeight) {
		this.componentWeight = componentWeight;
	}

	public String getExplain() {
		StringBuilder info = new StringBuilder();

		info.append("量化评价分析结果的分数由四部分构成，用户可以用过设置每个部分所占的比例来表达自己关心的程序结构特性。评价总分为100，每一个部分的取值范围为0~100。\n\n");
		
		info.append("抽象程度合理性的含义是：\n");
		info.append("被复用的组件应多设计些接口（或抽象类）来提高扩展性。\n");
		info.append("抽象程度合理性的公式是（1-分析单元汇总D值）*权值。\n");

		info.append("内聚性指数的含义是：\n");
		info.append("组件内的元素（JavaPackage或JavaClass）应该紧密。在复用目的的考虑下，组件内的子元素与其他组件的关系应能做到“相互抵消”。\n");
		info.append("内聚性指数的公式是（1 – 分析单元汇总Balance值）*权值。\n");

		info.append("封装性的含义是：\n");
		info.append("组件暴漏给其他组件可使用的接口应保证尽量的少，而实现这些接口的元素要做要隐藏，利于修改和扩展。\n");
		info.append("封装性的公式是（1 – 分析单元汇总Encapsulation值）*权值。\n");

		info.append("关系合理性的含义是：\n");
		info.append("组件间的关系应该是单向、合理，不存在彼此、循环、下层组件调用上层组件、稳定性强的组件依赖稳定性弱的组件的情况。\n");
		info.append("关系合理性的公式是（存在问题的关系 * 问题权值/总关系）*权值。\n");
		
		
		info.append("\n分数计算是否考虑组件大小的含义是：\n");
		info.append("在计算分数汇总组件指标的过程中，一种是不考虑组件的大小[代码行数]，直接求均值，另一种是考虑组件的大小，组件大的指标对最终结果的贡献值大。\n");
		
		return info.toString();
	}
}
