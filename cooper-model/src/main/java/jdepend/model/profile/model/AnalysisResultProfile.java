package jdepend.model.profile.model;

import java.io.Serializable;

public class AnalysisResultProfile implements Serializable {

	private static final long serialVersionUID = 4340413264825347906L;

	private Float distance;

	private Float balance;

	private Float encapsulation;

	private Float relationRationality;

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
	}

	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}

	public Float getEncapsulation() {
		return encapsulation;
	}

	public void setEncapsulation(Float encapsulation) {
		this.encapsulation = encapsulation;
	}

	public Float getRelationRationality() {
		return relationRationality;
	}

	public void setRelationRationality(Float relationRationality) {
		this.relationRationality = relationRationality;
	}
}
