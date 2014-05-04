package jdepend.knowledge.architectpattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import jdepend.model.Component;

public class ArchitectPatternResult {

	private Collection<Component> cores = new ArrayList<Component>();

	private Map<String, Collection<Component>> layers = new LinkedHashMap<String, Collection<Component>>();

	public ArchitectPatternResult(Collection<Component> cores, Map<String, Collection<Component>> layers) {
		super();
		this.cores = cores;
		this.layers = layers;
	}

	public Collection<Component> getCores() {
		return cores;
	}

	public Map<String, Collection<Component>> getLayers() {
		return layers;
	}

	public String getResult() {
		StringBuilder info = new StringBuilder();
		if (cores.size() > 0) {
			info.append(cores.size() + "核心：");
			for (Component core : cores) {
				info.append(core.getName());
				info.append(",");
			}
			info.delete(info.length() - 1, info.length());
			info.append("\n\n");
		}
		if (layers.size() > 0) {
			info.append(layers.size() + "层组件：\n");
			for (String layer : layers.keySet()) {
				for (Component component : layers.get(layer)) {
					info.append(component.getName());
					info.append(",");
				}
				info.delete(info.length() - 1, info.length());
				info.append("\n");
			}
			info.append("\n");
		}
		if (info.length() > 0) {
			return info.toString();
		} else {
			return null;
		}
	}
}
