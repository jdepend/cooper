package jdepend.framework.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class BusiLogItem {

	public String username;
	public String operation;
	public Date createdate;

	public Date getCreatedate() {
		return new Date(createdate.getTime()) {
			@Override
			public String toString() {
				return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(this);
			}
		};
	}
}
