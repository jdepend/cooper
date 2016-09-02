package jdepend.util.analyzer.framework;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class TwoDimensionData {

	private Map<String, ArrayList<Object>> datas = new LinkedHashMap<String, ArrayList<Object>>();

	public void setData(String column, Object value) {
		ArrayList<Object> columnData = datas.get(column);
		if (columnData == null) {
			columnData = new ArrayList<Object>();
			datas.put(column, columnData);
		}
		columnData.add(value);
	}

	public Map<String, ArrayList<Object>> getDatas() {
		return datas;
	}

	public boolean existData() {
		return !this.datas.isEmpty();
	}
}
