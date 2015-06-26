package jdepend.model.profile.model;

import java.io.Serializable;

public class AnalysisResultProfile implements Serializable {

	private static final long serialVersionUID = 4340413264825347906L;

	private float distance;

	private float balance;

	private float encapsulation;

	private float relationRationality;

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
}
