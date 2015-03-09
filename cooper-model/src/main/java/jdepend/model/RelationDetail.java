package jdepend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class RelationDetail implements Serializable {

	private static final long serialVersionUID = 7682002983026924611L;

	private float intensity = 0F;

	private Collection<JavaClassRelationItem> items = new ArrayList<JavaClassRelationItem>();

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public Collection<JavaClassRelationItem> getItems() {
		return items;
	}

	public void addItem(JavaClassRelationItem item) {
		this.items.add(item);
	}

	public void addItems(Collection<JavaClassRelationItem> items) {
		this.items.addAll(items);
	}
}
