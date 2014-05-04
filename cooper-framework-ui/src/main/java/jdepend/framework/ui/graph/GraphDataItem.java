package jdepend.framework.ui.graph;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public final class GraphDataItem implements Serializable {

	public static final String PIE = "PIE";// 饼图
	public static final String SPLINE = "SPLINE";// 折线图
	public static final String BAR = "BAR";// 柱状图

	private String title;

	private String type;

	private String group;

	private Map<Object, Object> datas = new LinkedHashMap<Object, Object>();

	private String lineName;

	private String lineXName;

	private String lineYName;

	private Map<Object, String> tips = new LinkedHashMap<Object, String>();

	private BgColorData bgColorData;

	private FgColorData fgColorData;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Map<Object, Object> getDatas() {
		return datas;
	}

	public void setDatas(Map<Object, Object> datas) {
		this.datas = datas;
	}

	public void addData(Object name, Object value) {
		this.datas.put(name, value);
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getLineXName() {
		return lineXName;
	}

	public void setLineXName(String lineXName) {
		this.lineXName = lineXName;
	}

	public String getLineYName() {
		return lineYName;
	}

	public void setLineYName(String lineYName) {
		this.lineYName = lineYName;
	}

	public Map<Object, String> getTips() {
		return tips;
	}

	public String getTip(Object name) {
		return this.tips.get(name);
	}

	public void setTips(Map<Object, String> tips) {
		this.tips = tips;
	}

	public void addTip(Object name, String tip) {
		this.tips.put(name, tip);
	}

	public BgColorData getBgColordData() {
		return bgColorData;
	}

	public void setBgColorData(BgColorData backgroundData) {
		this.bgColorData = backgroundData;
	}

	public FgColorData getFgColorData() {
		return fgColorData;
	}

	public void setFgColorData(FgColorData fgColorData) {
		this.fgColorData = fgColorData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		GraphDataItem other = (GraphDataItem) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
