package jdepend.client.report.way.htmlui;

import jdepend.client.report.way.textui.Printer;

public abstract class HTMLSortTablePrinter extends Printer {

	protected void printTableHeadElement(Object name) {
		getWriter().print("<th>");
		getWriter().print(name);
		getWriter().print("</th>");
	}

	protected void printTableRowElement(Object name) {
		getWriter().print("<td>");
		getWriter().print(name);
		getWriter().print("</td>");
	}

}
