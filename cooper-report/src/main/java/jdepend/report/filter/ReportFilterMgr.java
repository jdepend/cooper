package jdepend.report.filter;

public class ReportFilterMgr {

	private ReportFilter filter;
	private static ReportFilterMgr instance = new ReportFilterMgr();

	private ReportFilterMgr() {
	}

	public static ReportFilterMgr getInstance() {
		return instance;
	}

	public ReportFilter getReportFilter() {
		return filter;
	}

	public void setReportFilter(ReportFilter filter) {
		this.filter = filter;
	}

}
