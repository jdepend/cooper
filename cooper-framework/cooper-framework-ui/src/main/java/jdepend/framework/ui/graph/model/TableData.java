package jdepend.framework.ui.graph.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableData {

	private Map<String, ArrayList<Object>> datas = new LinkedHashMap<String, ArrayList<Object>>();

	private String sortColName;

	private int sortOperation = DESC;

	public static int DESC = -1;
	public static int ASC = 1;

	private String minColName;
	
	public TableData(){
		
	}
	
	public TableData(Map<String, ArrayList<Object>> datas){
		this.datas = datas;
	}

	public void setData(String column, Object value) {
		ArrayList<Object> columnData = datas.get(column);
		if (columnData == null) {
			columnData = new ArrayList<Object>();
			datas.put(column, columnData);
		}
		columnData.add(value);
	}

	public Collection<String> getColumnNames() {
		return this.datas.keySet();
	}

	public int getRows() {
		if (this.getColumnNames().size() == 0) {
			return 0;
		} else {
			return this.getColumnValues(
					(String) (new ArrayList<String>(this.getColumnNames()))
							.get(0)).size();
		}
	}

	public ArrayList<Object> getColumnValues(String column) {
		return this.datas.get(column);
	}

	public Object getColumnValue(String column, int row) {
		return this.getColumnValues(column).get(row);
	}

	public boolean existData() {
		return this.getRows() > 0;
	}

	public String getSortColName() {
		return sortColName;
	}

	public void setSortColName(String sortName) {
		this.sortColName = sortName;
	}

	public int getSortOperation() {
		return sortOperation;
	}

	public void setSortOperation(int sortOperation) {
		this.sortOperation = sortOperation;
	}

	public String getMinColName() {
		return minColName;
	}

	public void setMinColName(String minColName) {
		this.minColName = minColName;
	}

}
