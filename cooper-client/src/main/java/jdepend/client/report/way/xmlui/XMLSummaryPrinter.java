package jdepend.client.report.way.xmlui;

import jdepend.framework.util.MetricsFormat;
import jdepend.metadata.Named;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.client.report.way.textui.SummaryPrinter;

public final class XMLSummaryPrinter extends SummaryPrinter {
	@Override
	protected void printHeader(AnalysisResult inputData) {
		getWriter().println("<?xml version=\"1.0\"?>");
		getWriter().println("<Cooper>");
	}

	@Override
	protected void printFooter() {
		getWriter().println("</Cooper>");
	}

	@Override
	protected void printComponentsHeader() {
		getWriter().println(tab() + "<Units>");
	}

	@Override
	protected void printComponentsFooter() {
		getWriter().println(tab() + "</Units>");
	}

	@Override
	protected void printComponentHeader(JDependUnit jPackage) {
		printSectionBreak();
		getWriter().println(tab(2) + "<Unit name=\"" + jPackage.getName() + "\">");
	}

	@Override
	protected void printComponentFooter(JDependUnit jPackage) {
		getWriter().println(tab(2) + "</Unit>");
	}

	@Override
	protected void printNoStats() {
		getWriter().println(tab(3) + "<error>No stats available: " + "package referenced, but not analyzed.</error>");
	}

	@Override
	protected void printStatistics(JDependUnit jPackage) {
		getWriter().println(tab(3) + "<Stats>");
		getWriter().println(tab(4) + "<isInner>" + jPackage.isInner() + "</isInner>");
		getWriter().println(tab(4) + "<TotalClasses>" + jPackage.getClassCount() + "</TotalClasses>");
		getWriter().println(tab(4) + "<ConcreteClasses>" + jPackage.getConcreteClassCount() + "</ConcreteClasses>");
		getWriter().println(tab(4) + "<AbstractClasses>" + jPackage.getAbstractClassCount() + "</AbstractClasses>");
		getWriter().println(tab(4) + "<Ca>" + jPackage.getAfferentCoupling() + "</Ca>");
		getWriter().println(tab(4) + "<Ce>" + jPackage.getEfferentCoupling() + "</Ce>");
		getWriter().println(tab(4) + "<A>" + MetricsFormat.toFormattedMetrics(jPackage.getAbstractness()) + "</A>");
		getWriter().println(tab(4) + "<I>" + MetricsFormat.toFormattedMetrics(jPackage.getStability()) + "</I>");
		getWriter().println(
				tab(4) + "<Coupling>" + MetricsFormat.toFormattedMetrics(jPackage.getCoupling()) + "</Coupling>");
		getWriter().println(
				tab(4) + "<Cohesion>" + MetricsFormat.toFormattedMetrics(jPackage.getCohesion()) + "</Cohesion>");
		getWriter().println(
				tab(4) + "<Balance>" + MetricsFormat.toFormattedMetrics(jPackage.getBalance()) + "</Balance>");
		getWriter().println(tab(3) + "</Stats>");
	}

	@Override
	protected void printClassName(JavaClassUnit jClass) {
		getWriter().println(tab(4) + "<Class>");
		getWriter().println(tab(5) + jClass.getName());
		getWriter().println(tab(4) + "</Class>");
	}

	@Override
	protected void printPackageName(JDependUnit jPackage) {
		getWriter().println(tab(4) + "<Unit>" + jPackage.getName() + "</Unit>");
	}

	@Override
	protected void printAbstractClassesHeader() {
		getWriter().println(tab(3) + "<AbstractClasses>");
	}

	@Override
	protected void printAbstractClassesFooter() {
		getWriter().println(tab(3) + "</AbstractClasses>");
	}

	@Override
	protected void printConcreteClassesHeader() {
		getWriter().println(tab(3) + "<ConcreteClasses>");
	}

	@Override
	protected void printConcreteClassesFooter() {
		getWriter().println(tab(3) + "</ConcreteClasses>");
	}

	@Override
	protected void printEfferentsHeader() {
		getWriter().println(tab(3) + "<DependsUpon>");
	}

	@Override
	protected void printEfferentsFooter() {
		getWriter().println(tab(3) + "</DependsUpon>");
	}

	@Override
	protected void printAfferentsHeader() {
		getWriter().println(tab(3) + "<UsedBy>");
	}

	@Override
	protected void printAfferentsFooter() {
		getWriter().println(tab(3) + "</UsedBy>");
	}

	@Override
	protected void printCyclesHeader() {
		printSectionBreak();
		getWriter().println(tab() + "<Cycles>");
	}

	@Override
	protected void printCyclesFooter() {
		getWriter().println(tab() + "</Cycles>");
	}

	@Override
	protected void printCycleHeader(Named jPackage) {
		getWriter().println(tab(2) + "<Unit Name=\"" + jPackage.getName() + "\">");
	}

	@Override
	protected void printCycleFooter() {
		getWriter().println(tab(2) + "</Unit>");
		printSectionBreak();
	}

	@Override
	protected void printCycleTarget(Named jPackage) {
		printCycleContributor(jPackage);
	}

	@Override
	protected void printCycleContributor(Named jPackage) {
		getWriter().println(tab(3) + "<Unit>" + jPackage.getName() + "</Unit>");
	}
}
