package jdepend.knowledge.motive;

import java.io.Serializable;
import java.util.Date;

public class MotiveOperation implements Serializable {

	private static final long serialVersionUID = 9024335641681058909L;

	private String desc;
	private Date date;

	public MotiveOperation() {
		super();
	}

	public MotiveOperation(String desc, Date date) {
		super();
		this.desc = desc;
		this.date = date;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
