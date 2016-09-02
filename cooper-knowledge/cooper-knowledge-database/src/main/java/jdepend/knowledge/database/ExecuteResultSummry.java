package jdepend.knowledge.database;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import jdepend.model.result.AnalysisResultSummary;

public final class ExecuteResultSummry implements Serializable {

	private String id;

	private Date createDate;

	private AnalysisResultSummary summry;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return new Date(createDate.getTime()) {
			@Override
			public String toString() {
				return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(this);
			}
		};
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public AnalysisResultSummary getSummry() {
		return summry;
	}

	public void setSummry(AnalysisResultSummary summry) {
		this.summry = summry;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExecuteResultSummry other = (ExecuteResultSummry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
