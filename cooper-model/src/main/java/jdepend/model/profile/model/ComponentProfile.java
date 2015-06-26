package jdepend.model.profile.model;

import java.io.Serializable;

public class ComponentProfile implements Serializable {

	private static final long serialVersionUID = -2299662426980399420L;

	private String stability;

	private String distance;

	private String encapsulation;

	private String balance;

	public String getStability() {
		return stability;
	}

	public void setStability(String stability) {
		this.stability = stability;
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

}
