package jdepend.ui.result;

import jdepend.report.ReportCreator;
import jdepend.report.ReportCreatorFactory;

public class JDependReportAdapterFactory implements ReportCreatorFactory {

	public ReportCreator create(String group, String command) {
		return new JDependReport(group, command);
	}

}
