package jdepend.knowledge.architectpattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jdepend.model.Component;

public class ArchitectPatternWorker {

	private List<Component> cores = new ArrayList<Component>();

	private Map<String, Collection<Component>> layers = new LinkedHashMap<String, Collection<Component>>();

	public void addCoreComponent(Component component) {
		if (!this.cores.contains(component)) {
			this.cores.add(component);
		}
	}

	public Map<String, Collection<Component>> getLayers() {
		return layers;
	}

	public void setLayers(Map<String, Collection<Component>> layers) {
		this.layers = layers;
	}

	public List<Component> getCores() {
		return cores;
	}
}
