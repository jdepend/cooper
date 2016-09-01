package jdepend.client.report.history;

import java.util.Date;

public class ReportHistoryItemInfo {

	public String key;

	public Date date;

	public String tip;

	public String path;

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof ReportHistoryItemInfo) {
			return this.key.equals(((ReportHistoryItemInfo) arg0).key);
		} else {
			return false;
		}
	}

}
