package jdepend.model;

import java.util.ArrayList;
import java.util.List;

public class GroupCohesionInfo {

	private List<GroupCohesionItem> groupCohesionItems = new ArrayList<GroupCohesionItem>();

	private Float cohesion = 0F;

	public List<GroupCohesionItem> getGroupCohesionItems() {
		return groupCohesionItems;
	}

	public void setGroupCohesionItems(List<GroupCohesionItem> groupCohesionItems) {
		this.groupCohesionItems = groupCohesionItems;
	}

	public Float getCohesion() {
		return cohesion;
	}

	public void setCohesion(Float cohesion) {
		this.cohesion = cohesion;
	}
}
