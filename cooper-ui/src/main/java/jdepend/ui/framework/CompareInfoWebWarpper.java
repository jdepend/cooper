package jdepend.ui.framework;

import java.awt.Color;

import jdepend.util.refactor.CompareInfo;

public class CompareInfoWebWarpper {

	private CompareInfo compareInfo;

	public CompareInfoWebWarpper(CompareInfo compareInfo) {
		super();
		this.compareInfo = compareInfo;
	}

	public Color getDirectionColor() {
		Boolean evaluate = compareInfo.getEvaluate();
		if (evaluate == null) {
			return Color.blue;
		} else if (evaluate) {
			return Color.green;
		} else {
			return Color.red;
		}
	}

	public String getCompare() {
		int compare = compareInfo.getResult();
		if (compare == CompareInfo.NEW) {
			return "-";
		} else if (compare < 0) {
			return "↓";
		} else if (compare > 0) {
			return "↑";
		} else {
			return "";
		}
	}

}
