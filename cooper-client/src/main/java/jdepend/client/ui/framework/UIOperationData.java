package jdepend.client.ui.framework;

import java.io.Serializable;

public class UIOperationData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7651447289200894010L;

	private int defaultTabOneIndex;

	private int defaultTabTwoIndex;

	private Integer groupIndex = -1;

	public int getDefaultTabOneIndex() {
		return defaultTabOneIndex;
	}

	public void setDefaultTabOneIndex(int defaultTabOneIndex) {
		this.defaultTabOneIndex = defaultTabOneIndex;
	}

	public int getDefaultTabTwoIndex() {
		return defaultTabTwoIndex;
	}

	public void setDefaultTabTwoIndex(int defaultTabTwoIndex) {
		this.defaultTabTwoIndex = defaultTabTwoIndex;
	}

	public Integer getGroupIndex() {
		return groupIndex;
	}

	public void setGroupIndex(Integer groupIndex) {
		this.groupIndex = groupIndex;
	}

}
