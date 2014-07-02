package jdepend.report.way.htmlui;

import java.util.Collection;

import jdepend.framework.util.MetricsFormat;
import jdepend.model.Relation;
import jdepend.report.util.ReportConstant;

public class HTMLRelationPrinter extends HTMLSortTablePrinter {

	public void printRelationList(Collection<Relation> relations) {

		getWriter().println("<table id=\"RelationList\" class=\"sortable\">");
		getWriter().println("<tr>");
		this.printTableHeadElement(ReportConstant.Relation_CurrentName);
		this.printTableHeadElement(ReportConstant.Relation_DependName);
		this.printTableHeadElement(ReportConstant.Relation_Intensity);
		this.printTableHeadElement(ReportConstant.Relation_CurrentCohesion);
		this.printTableHeadElement(ReportConstant.Relation_DependCohesion);
		this.printTableHeadElement(ReportConstant.Relation_Balance);
		this.printTableHeadElement(ReportConstant.Relation_AttentionType);
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
