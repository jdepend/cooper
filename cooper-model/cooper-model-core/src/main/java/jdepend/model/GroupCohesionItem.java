package jdepend.model;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.framework.util.MetricsFormat;
import jdepend.metadata.JavaClassRelationItem;

public final class GroupCohesionItem implements Comparable<GroupCohesionItem> {

	public String name;
	public Float cohesion;

	public Collection<JavaClassRelationItem> javaClassRelationItems = new ArrayList<JavaClassRelationItem>();

	public GroupCohesionItem(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Float getCohesion() {
		if (this.cohesion == null) {
			this.cohesion = 0F;
			for (JavaClassRelationItem item : javaClassRelationItems) {
				this.cohesion += item.getRelationIntensity();
			}
		}
		return cohesion;
	}

	public Collection<JavaClassRelationItem> getJavaClassRelationItems() {
		return javaClassRelationItems;
	}

	public void addDetail(Collection<JavaClassRelationItem> javaClassRelationItems) {
		this.javaClassRelationItems.addAll(javaClassRelationItems);
	}

	public void addItem(JavaClassRelationItem item) {
		this.javaClassRelationItems.add(item);
	}

	@Override
	public String toString() {
		StringBuilder info = new StringBuilder();

		info.append("GroupCohesionItem [cohesion=");
		info.append(MetricsFormat.toFormattedMetrics(cohesion));
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
	public int compareTo(GroupCohesionItem o) {
		return new Float(cohesion).compareTo(o.cohesion);
	}
}
