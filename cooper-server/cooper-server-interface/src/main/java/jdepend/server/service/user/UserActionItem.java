package jdepend.server.service.user;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class UserActionItem implements Serializable {

	public String username;
	public String operation;
	public String ip;
	public Date createdate;
	public Date gartherdate;

	public UserActionItem() {
	}

	public UserActionItem(String username, String operation, String ip, Date createdate) {
		super();
		this.username = username;
		this.operation = operation;
		this.ip = ip;
		this.createdate = createdate;
	}

	public Date getCreatedate() {
		return new Date(createdate.getTime()) {
			@Override
			public String toString() {
				return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(this);
			}
		};
	}

	public Date getGartherdate() {
		return new Date(gartherdate.getTime()) {
			@Override
			public String toString() {
				return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(this);
			}
		};
	}

}
