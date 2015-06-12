package jdepend.client.report.model;

import jdepend.model.JavaClassUnit;
import jdepend.model.MetricsMgr;

public class ReportJavaClassUnit extends JavaClassUnit {

	public ReportJavaClassUnit(JavaClassUnit javaClassUnit) {
		super(javaClassUnit);
	}

	@Override
	public Object getValue(String metrics) {

		switch (metrics) {
		case MetricsMgr.Ca:
			return this.getAfferentCoupling() + "|" + this.getCaList().size() + " <";

		case MetricsMgr.Ce:
			return this.getEfferentCoupling() + "|" + this.getCeList().size() + " >";

		default:
			return super.getValue(metrics);
		}

	}
}
