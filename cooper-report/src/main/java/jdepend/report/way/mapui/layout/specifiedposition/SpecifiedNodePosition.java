package jdepend.report.way.mapui.layout.specifiedposition;

import java.io.Serializable;

public class SpecifiedNodePosition implements Serializable{

	private static final long serialVersionUID = 7240600446128641330L;

	private String name;
	private double xField;
	private double yField;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getxField() {
		return xField;
	}

	public void setxField(double xField) {
		this.xField = xField;
	}

	public double getyField() {
		return yField;
	}

	public void setyField(double yField) {
		this.yField = yField;
	}

}
