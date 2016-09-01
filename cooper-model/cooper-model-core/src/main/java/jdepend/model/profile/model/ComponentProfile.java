package jdepend.model.profile.model;

import java.io.Serializable;

public class ComponentProfile implements Serializable {

	private static final long serialVersionUID = -2299662426980399420L;

	private float stabilityWithCountScale;

	private String distance;

	private String encapsulation;

	private String balance;

	public static final String balanceFromPackage = "采用包作为计算内聚性的子元素";
	public static final String balanceFromClass = "采用类作为计算内聚性的子元素";

	public float getStabilityWithCountScale() {
		return stabilityWithCountScale;
	}

	public void setStabilityWithCountScale(float stabilityWithCountScale) {
		this.stabilityWithCountScale = stabilityWithCountScale;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getEncapsulation() {
		return encapsulation;
	}

	public void setEncapsulation(String encapsulation) {
		this.encapsulation = encapsulation;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getExplain() {
		StringBuilder info = new StringBuilder();
		
		info.append("[组件稳定性]是由组件关系计算得到的（I=Ce/(Ca+Ce)）。组件关系有数量和强度两个维度，用户可以通过设置采用数量、强度，或者数量和强度来计算组件的稳定性。\n\n");
	
		info.append("[组件内聚性]的计算依赖将何种粒度的元素作为子元素。系统提供两种选项，分别是包和类。当采用包作为子元素的时候，组件内包之间的关系越紧密内聚性越强，同理，采用类作为子元素的时候，组件内类之间的关系越紧密内聚性越强。\n\n");
		info.append("以包作为子元素，粒度比较大，更能够表现大的结构，缺点是这依赖于组件内包的组织形式，用户可以通过仅仅改变包包含的内容来提高内聚性。\n\n");
		info.append("以类作为子元素，粒度比较小，反应结构的特征有限，好处是这不依赖组件内包的组织形式。\n\n");

		return info.toString();
	}

}
