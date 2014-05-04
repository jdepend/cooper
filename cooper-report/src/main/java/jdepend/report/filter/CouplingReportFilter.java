package jdepend.report.filter;

public class CouplingReportFilter extends ReportFilter {

	public boolean isInnerClass() {
		if (switchInfo.size() == 0)
			return true;

		return "true".equals(switchInfo.get("InnerClass")) || "1".equals(switchInfo.get("InnerClass"))
				|| "Y".equalsIgnoreCase((String) switchInfo.get("InnerClass"));
	}

	public boolean isExpertClass() {
		if (switchInfo.size() == 0)
			return true;

		return "true".equals(switchInfo.get("ExpertClass")) || "1".equals(switchInfo.get("ExpertClass"))
				|| "Y".equalsIgnoreCase((String) switchInfo.get("ExpertClass"));
	}

}
