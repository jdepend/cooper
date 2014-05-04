package jdepend.report.way.htmlui;

import java.util.Collection;

import jdepend.framework.util.MetricsFormat;
import jdepend.model.Relation;
import jdepend.report.util.ReportConstant;

public class HTMLRelationPrinter extends HTMLSortTablePrinter {

	public void printRelationList(Collection<Relation> relations) {

		getWriter().println("<table id=\"RelationList\" class=\"sortable\">");
		getWriter().println("<tr>");
		this.printTableHeadElement(ReportConstant.CurrentElement);
		this.printTableHeadElement(ReportConstant.DependElement);
		this.printTableHeadElement(ReportConstant.Intensity);
		this.printTableHeadElement(ReportConstant.Current);
		this.printTableHeadElement(ReportConstant.Depend);
		this.printTableHeadElement(ReportConstant.RelationBalance);
		this.printTableHeadElement(ReportConstant.RelationAttentionType);
		getWriter().println("\n</tr>");

		for (Relation relation : relations) {
			getWriter().println("<tr>");
			this.printTableRowElement(relation.getCurrent().getName());
			this.printTableRowElement(relation.getDepend().getName());
			this.printTableRowElement(MetricsFormat.toFormattedMetrics(relation.getIntensity()));
			this.printTableRowElement(MetricsFormat.toFormattedMetrics(relation.getCurrent().getIntensity()));
			this.printTableRowElement(MetricsFormat.toFormattedMetrics(relation.getDepend().getIntensity()));
			this.printTableRowElement(MetricsFormat.toFormattedMetrics(relation.getBalance()));
			this.printTableRowElement(Relation.AttentionTypeList.get(relation.getAttentionType()));
			getWriter().println("\n</tr>");
		}
		getWriter().println("</table>");

		getWriter().flush();
	}

}
