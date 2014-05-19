package jdepend.report.way.textui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClass;
import jdepend.model.Named;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JDependUnitComparatorMgr;

public abstract class SummaryPrinter extends Printer {

	/**
	 * 打印小结信息
	 * 
	 * @param inputData
	 */
	public final void printBasic(AnalysisResult inputData) {

		printHeader(inputData);

		printPackages(inputData.getComponents());

		printCycles(inputData.getComponents());

		printSummary(inputData);

		printFooter();

		getWriter().flush();
	}

	protected void printPackages(Collection units) {
		printPackagesHeader();

		Iterator i = units.iterator();
		while (i.hasNext()) {
			printPackage((JDependUnit) i.next());
		}

		printPackagesFooter();
	}

	public void printPackage(JDependUnit unit) {

		printPackageHeader(unit);

		if (unit.getClasses().size() == 0) {
			printNoStats();
			printPackageFooter(unit);
			return;
		}

		printStatistics(unit);

		printSectionBreak();

		printAbstractClasses(unit);

		printSectionBreak();

		printConcreteClasses(unit);

		printSectionBreak();

		printEfferents(unit);

		printSectionBreak();

		printAfferents(unit);

		printPackageFooter(unit);
	}

	protected void printAbstractClasses(JDependUnit unit) {
		printAbstractClassesHeader();

		List<JavaClass> members = new ArrayList<JavaClass>(unit.getClasses());
		Collections.sort(members);
		Iterator<JavaClass> memberIter = members.iterator();
		while (memberIter.hasNext()) {
			JavaClass jClass = memberIter.next();
			if (jClass.isAbstract()) {
				printClassName(jClass);
			}
		}

		printAbstractClassesFooter();
	}

	protected void printConcreteClasses(JDependUnit unit) {
		printConcreteClassesHeader();

		List<JavaClass> members = new ArrayList<JavaClass>(unit.getClasses());
		Collections.sort(members);
		Iterator<JavaClass> memberIter = members.iterator();
		while (memberIter.hasNext()) {
			JavaClass concrete = (JavaClass) memberIter.next();
			if (!concrete.isAbstract()) {
				printClassName(concrete);
			}
		}

		printConcreteClassesFooter();
	}

	protected void printEfferents(JDependUnit unit) {
		printEfferentsHeader();

		Collection efferents1 = unit.getEfferents();

		List<JDependUnit> efferents = new ArrayList<JDependUnit>(efferents1);

		Collections.sort(efferents, JDependUnitComparatorMgr.getComparator());
		Iterator efferentIter = efferents.iterator();
		while (efferentIter.hasNext()) {
			JDependUnit efferent = (JDependUnit) efferentIter.next();
			printPackageName(efferent);
		}
		if (efferents.size() == 0) {
			printEfferentsError();
		}

		printEfferentsFooter();
	}

	protected void printAfferents(JDependUnit unit) {
		printAfferentsHeader();

		Collection afferents1 = unit.getAfferents();

		List<JDependUnit> afferents = new ArrayList<JDependUnit>(afferents1);

		Collections.sort(afferents, JDependUnitComparatorMgr.getComparator());
		Iterator afferentIter = afferents.iterator();
		while (afferentIter.hasNext()) {
			JDependUnit afferent = (JDependUnit) afferentIter.next();
			printPackageName(afferent);
		}
		if (afferents.size() == 0) {
			printAfferentsError();
		}

		printAfferentsFooter();
	}

	protected void printCycles(Collection<Component> units) {
		printCyclesHeader();

		for (JDependUnit unit : units) {
			printCycle(unit);
		}
		printCyclesFooter();
	}

	public void printCycle(JDependUnit unit) {

		if (!unit.getContainsCycle()) {
			return;
		}

		List<JDependUnit> list = unit.collectCycle();

		JDependUnit cyclePackage = (JDependUnit) list.get(list.size() - 1);
		String cyclePackageName = cyclePackage.getName();

		int i = 0;
		Iterator<JDependUnit> pkgIter = list.iterator();
		while (pkgIter.hasNext()) {
			i++;
			JDependUnit pkg = pkgIter.next();
			if (i == 1) {
				printCycleHeader(pkg);
			} else {
				if (pkg.getName().equals(cyclePackageName)) {
					printCycleTarget(pkg);
				} else {
					printCycleContributor(pkg);
				}
			}
		}
		printCycleFooter();
	}

	protected void printHeader(AnalysisResult inputData) {
		// do nothing
	}

	protected void printFooter() {
		// do nothing
	}

	protected void printPackagesHeader() {
		// do nothing
	}

	protected void printPackagesFooter() {
		// do nothing
	}

	protected void printNoStats() {
	}

	protected void printPackageHeader(JDependUnit unit) {
	}

	protected void printPackageFooter(JDependUnit unit) {
		// do nothing
	}

	protected void printStatistics(JDependUnit unit) {
	}

	protected void printClassName(JavaClass jClass) {
	}

	protected void printPackageName(JDependUnit unit) {
	}

	protected void printAbstractClassesHeader() {
	}

	protected void printAbstractClassesFooter() {
		// do nothing
	}

	protected void printConcreteClassesHeader() {
	}

	protected void printConcreteClassesFooter() {
		// do nothing
	}

	protected void printEfferentsHeader() {
	}

	protected void printEfferentsFooter() {
		// do nothing
	}

	protected void printEfferentsError() {
	}

	protected void printAfferentsHeader() {
	}

	protected void printAfferentsFooter() {
		// do nothing
	}

	protected void printAfferentsError() {
	}

	protected void printCyclesHeader() {
	}

	protected void printCyclesFooter() {
		// do nothing
	}

	protected void printCycleHeader(Named unit) {
	}

	protected void printCycleTarget(Named unit) {
	}

	protected void printCycleContributor(Named unit) {
	}

	protected void printCycleFooter() {
		printSectionBreak();
	}

	protected void printSummary(AnalysisResult inputData) {
	}

	protected void printSectionBreak() {
	}

	protected String tab() {
		return "    ";
	}

	protected String tab(int n) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < n; i++) {
			s.append(tab());
		}

		return s.toString();
	}
}
