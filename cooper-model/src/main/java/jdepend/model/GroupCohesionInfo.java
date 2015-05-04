package jdepend.model;

import java.util.List;

public class GroupCohesionInfo {

	private List<GroupCohesionItem> groupCohesionItems;

	private Float cohesion;

	public List<GroupCohesionItem> getGroupCohesionItems() {
		return groupCohesionItems;
	}

	public void setGroupCohesionItems(List<GroupCohesionItem> groupCohesionItems) {
		this.groupCohesionItems = groupCohesionItems;
	}

	public Float getCohesion() {
		if (this.cohesion == null) {
			this.cohesion = 0F;
			for (GroupCohesionItem item : groupCohesionItems) {
				this.cohesion += item.getCohesion();
			}
		}
		return this.cohesion;
	}
}
