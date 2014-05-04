package jdepend.report.way.htmlui;

import java.util.Collection;

import jdepend.model.JavaPackage;

public class HTMLListPackagePrinter extends HTMLSortTablePrinter {

	public void printPackageList(Collection<JavaPackage> javaPackages) {

		getWriter().println("<table id=\"JavaPackageList\" class=\"sortable\">");
		getWriter().println("<tr>");
		this.printTableHeadElement("名称");
		getWriter().println("\n</tr>");

		for (JavaPackage javaPackage : javaPackages) {
			getWriter().println("<tr>");
			this.printTableRowElement(javaPackage.getName());
			getWriter().println("\n</tr>");
		}
		getWriter().println("</table>");

		getWriter().flush();
	}

}
