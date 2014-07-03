package jdepend.model.area;

import java.util.ArrayList;
import java.util.Collection;

import jdepend.model.Component;

public class AreaComponentInfo {

	private Integer layer;
	private String name;
	private Collection<Component> components = new ArrayList<Component>();

	public Integer getLayer() {
		return layer;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Component> getComponents() {
		return components;
	}

	public void setComponents(Collection<Component> components) {
		this.components = components;
	}

}
