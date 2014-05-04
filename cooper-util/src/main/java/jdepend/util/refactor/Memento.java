package jdepend.util.refactor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jdepend.model.result.AnalysisResult;

public class Memento implements Serializable {

	private AnalysisResult result;

	private List<String> actions;

	private Date createDate;

	public Memento(AnalysisResult result, List<String> actions) {
		this.result = result;
		this.actions = actions;
		this.createDate = new Date();
	}

	public List<String> getActions() {
		return actions;
	}

	public AnalysisResult getResult() {
		return result;
	}

	public Date getCreateDate() {
		return new Date(this.createDate.getTime()) {
			public String toString() {
				return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(this);
			}
		};
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
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
		Memento other = (Memento) obj;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		return true;
	}

}
