package jdepend.report.way.mapui.layout.specifiedposition;

import java.io.Serializable;

public class SpecifiedNodePosition implements Serializable {

	private static final long serialVersionUID = 7240600446128641330L;

	private String name;
	private double x;
	private double y;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
