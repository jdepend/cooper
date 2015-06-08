package jdepend.report.model;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.model.JavaClassUnit;

public class ReportJavaClassUnitUtil {

	public static Collection<ReportJavaClassUnit> convert(Collection<JavaClassUnit> javaClasses) {
		Collection<ReportJavaClassUnit> reportJavaClasses = new ArrayList<ReportJavaClassUnit>();
		for (JavaClassUnit javaClass : javaClasses) {
			reportJavaClasses.add(new ReportJavaClassUnit(javaClass));
		}
		return reportJavaClasses;
	}

}
