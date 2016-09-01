package jdepend.client.report.way.textui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jdepend.metadata.Named;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;

public abstract class SummaryPrinter extends Printer {

	/**
	 * 打印小结信息
	 * 
	 * @param inputData
	 */
	public final void printBasic(AnalysisResult inputData) {

		printHeader(inputData);

		printComponents(inputData.getComponents());

		printCycles(inputData.getComponents());

		printSummary(inputData);

		printFooter();

		getWriter().flush();
	}

	protected void printComponents(Collection<Component> components) {
		printComponentsHeader();

		Iterator<Component> i = components.iterator();
		while (i.hasNext()) {
			printComponent(i.next());
		}

		printComponentsFooter();
	}

	public void printComponent(Component component) {

		printComponentHeader(component);

		if (component.getClasses().size() == 0) {
			printNoStats();
			printComponentFooter(component);
			return;
		}

		printStatistics(component);

		printSectionBreak();

		printAbstractClasses(component);

		printSectionBreak();

		printConcreteClasses(component);

		printSectionBreak();

		printEfferents(component);

		printSectionBreak();

		printAfferents(component);

		printComponentFooter(component);
	}

	protected void printAbstractClasses(Component unit) {
		printAbstractClassesHeader();

		List<JavaClassUnit> members = new ArrayList<JavaClassUnit>(unit.getClasses());
		Collections.sort(members);
		Iterator<JavaClassUnit> memberIter = members.iterator();
		while (memberIter.hasNext()) {
			JavaClassUnit jClass = memberIter.next();
			if (jClass.getJavaClass().isAbstract()) {
				printClassName(jClass);
			}
		}

		printAbstractClassesFooter();
	}

	protected void printConcreteClasses(Component unit) {
		printConcreteClassesHeader();

		List<JavaClassUnit> members = new ArrayList<JavaClassUnit>(unit.getClasses());
		Collections.sort(members);
		Iterator<JavaClassUnit> memberIter = members.iterator();
		while (memberIter.hasNext()) {
			JavaClassUnit concrete = (JavaClassUnit) memberIter.next();
			if (!concrete.getJavaClass().isAbstract()) {
				printClassName(concrete);
			}
		}

		printConcreteClassesFooter();
	}

	protected void printEfferents(Component unit) {
		printEfferentsHeader();

		Collection<Component> efferents1 = unit.getEfferents();

		List<Component> efferents = new ArrayList<Component>(efferents1);

		Collections.sort(efferents);
		Iterator<Component> efferentIter = efferents.iterator();
		while (efferentIter.hasNext()) {
			printComponentName(efferentIter.next());
		}
		if (efferents.size() == 0) {
			printEfferentsError();
		}

		printEfferentsFooter();
	}

	protected void printAfferents(Component unit) {
		printAfferentsHeader();

		Collection<Component> afferents1 = unit.getAfferents();

		List<Component> afferents = new ArrayList<Component>(afferents1);

		Collections.sort(afferents);
		Iterator<Component> afferentIter = afferents.iterator();
		while (afferentIter.hasNext()) {
			printComponentName(afferentIter.next());
		}
		if (afferents.size() == 0) {
			printAfferentsError();
		}

		printAfferentsFooter();
	}

	protected void printCycles(Collection<Component> units) {
		printCyclesHeader();

		for (Component unit : units) {
			printCycle(unit);
		}
		printCyclesFooter();
	}

	public void printCycle(JDependUnit unit) {

		if (!unit.getContainsCycle()) {
			return;
		}

		List<? extends JDependUnit> list = unit.collectCycle();

		JDependUnit cycleUnit = (JDependUnit) list.get(list.size() - 1);
		String cycleUnitName = cycleUnit.getName();

		int i = 0;
		Iterator<? extends JDependUnit> unitIter = list.iterator();
		while (unitIter.hasNext()) {
			i++;
			JDependUnit pkg = unitIter.next();
			if (i == 1) {
				printCycleHeader(pkg);
			} else {
				if (pkg.getName().equals(cycleUnitName)) {
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

	protected void printComponentsHeader() {
		// do nothing
	}

	protected void printComponentsFooter() {
		// do nothing
	}

	protected void printNoStats() {
	}

	protected void printComponentHeader(Component unit) {
	}

	protected void printComponentFooter(Component unit) {
		// do nothing
	}

	protected void printStatistics(Component unit) {
	}

	protected void printClassName(JavaClassUnit jClass) {
	}

	protected void printComponentName(Component unit) {
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
