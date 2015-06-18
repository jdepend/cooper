package jdepend.framework.ui.graph.model;

import java.io.Serializable;

public final class TableCallBack implements Serializable {

	public String colName;
	public String DialogName;

	public TableCallBack(String colName, String dialogName) {
		super();
		this.colName = colName;
		DialogName = dialogName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colName == null) ? 0 : colName.hashCode());
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
		TableCallBack other = (TableCallBack) obj;
		if (colName == null) {
			if (other.colName != null)
				return false;
		} else if (!colName.equals(other.colName))
			return false;
		return true;
	}

}
