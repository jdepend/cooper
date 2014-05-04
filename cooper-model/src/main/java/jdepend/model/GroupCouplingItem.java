package jdepend.model;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.framework.util.MetricsFormat;

public final class GroupCouplingItem implements Comparable<GroupCouplingItem> {

	public String name;
	public float coupling;

	public Collection<JavaClassRelationItem> javaClassRelationItems = new ArrayList<JavaClassRelationItem>();

	public GroupCouplingItem(String name, float coupling) {
		super();
		this.name = name;
		this.coupling = coupling;
	}

	public void addDetail(Collection<JavaClassRelationItem> javaClassRelationItems) {
		this.javaClassRelationItems.addAll(javaClassRelationItems);
	}

	@Override
	public String toString() {
		StringBuilder info = new StringBuilder();

		info.append("GroupCouplingInfo [coupling=");
		info.append(MetricsFormat.toFormattedMetrics(coupling));
		info.append(", name=");
		info.append(name);
		info.append("]");

		for (JavaClassRelationItem javaClassRelationItem : javaClassRelationItems) {
			info.append("\n	");
			info.append(javaClassRelationItem);
		}
		return info.toString();
	}

	@Override
	public int compareTo(GroupCouplingItem o) {
		return new Float(coupling).compareTo(o.coupling);
	}
}
