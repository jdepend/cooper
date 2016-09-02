package jdepend.util.refactor;

import java.util.ArrayList;
import java.util.Collection;

final class UniteComponentConf {

	private String name;
	private int layer;

	private Collection<String> components = new ArrayList<String>();

	public UniteComponentConf(String name, int layer, Collection<String> components) {
		super();
		this.name = name;
		this.layer = layer;
		this.components = components;
	}

	public String getName() {
		return name;
	}

	public int getLayer() {
		return layer;
	}

	public Collection<String> getComponents() {
		return components;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		final UniteComponentConf other = (UniteComponentConf) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
