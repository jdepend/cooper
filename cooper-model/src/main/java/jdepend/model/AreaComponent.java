package jdepend.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;


public class AreaComponent implements Comparable<AreaComponent> {

	private Integer layer;
	private String name;
	private Float instability;
	private Collection<String> components;

	private Collection<Component> componentList = new HashSet<Component>();
	private Collection<Component> afferents = null;// 缓存
	private Collection<Component> efferents = null;// 缓存

	public AreaComponent() {

	}

	public AreaComponent(Integer layer, String name) {
		super();
		this.layer = layer;
		this.name = name;
		this.components = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLayer() {
		return layer;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	public Float instability() {
		if (this.instability == null) {
			int ca = this.getAfferents().size();
			int ce = this.getEfferents().size();
			if (ce == 0) {
				this.instability = 0F;
			} else if (ca + ce == 0) {
				this.instability = 0.5F;
			} else {
				this.instability = ce * 1F / (ca + ce);
			}
		}
		return this.instability;
	}

	public Collection<Component> getAfferents() {

		if (this.afferents == null) {
			this.afferents = new HashSet<Component>();
			for (Component component : this.componentList) {
				for (Component afferent : component.getAfferents()) {
					if (!this.afferents.contains(afferent) && !this.componentList.contains(afferent)) {
						this.afferents.add(afferent);
					}
				}
			}
		}
		return this.afferents;
	}

	public Collection<Component> getEfferents() {

		if (this.efferents == null) {
			this.efferents = new HashSet<Component>();
			for (Component component : this.componentList) {
				for (Component efferent : component.getEfferents()) {
					if (!this.efferents.contains(efferent) && !this.componentList.contains(efferent)) {
						this.efferents.add(efferent);
					}
				}
			}
		}
		return this.efferents;
	}

	public Collection<String> getComponents() {
		return components;
	}

	public void addComponent(Component component) {
		if (!this.componentList.contains(component)) {
			this.componentList.add(component);
			this.components.add(component.getName());

			component.setAreaComponent(this);
			this.clear();
		}
	}

	public void deleteComponent(String componentName) {
		this.components.remove(componentName);
		for (Component component : this.componentList) {
			if (component.getName().equals(componentName)) {
				this.componentList.remove(component);
				component.setAreaComponent(null);
				break;
			}
		}
		this.clear();

	}

	public Collection<Component> getComponentList() {
		return componentList;
	}

	public void setComponentList(Collection<Component> componentList) {
		this.componentList = componentList;
		this.components = new ArrayList<String>();

		for (Component component : componentList) {
			component.setAreaComponent(this);
			this.components.add(component.getName());
		}
		this.clear();
	}

	public void clear() {
		this.afferents = null;
		this.efferents = null;
		this.instability = null;
	}

	@Override
	public String toString() {
		return "AreaComponent [layer=" + layer + ", name=" + name + ", instability=" + instability + "]";
	}

	@Override
	public int compareTo(AreaComponent o) {
		return o.instability().compareTo(this.instability());
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
		AreaComponent other = (AreaComponent) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
