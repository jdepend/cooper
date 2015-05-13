package jdepend.model.component.modelconf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jdepend.model.JavaClass;
import jdepend.model.JavaClassUnit;

/**
 * 组件模型下一个组件的配置信息
 * 
 * @author wangdg
 * 
 */
public abstract class ComponentConf implements Serializable, Cloneable {

	private String name;
	private int layer;

	private List<String> itemIds = new ArrayList<String>();

	public ComponentConf(String name) {
		super();
		this.name = name;
	}

	public ComponentConf(String name, int layer) {
		super();
		this.name = name;
		this.layer = layer;
	}

	public ComponentConf(String name, List<String> itemIds) {
		super();
		this.name = name;
		this.itemIds = itemIds;
	}

	public String getName() {
		return name;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public Collection<String> getItemIds() {
		return itemIds;
	}

	public void addItemIds(List<String> joinItemIds) {
		for (String itemid : joinItemIds) {
			this.addItemId(itemid);
		}
	}

	public void addItemId(String itemId) {
		if (!itemIds.contains(itemId)) {
			itemIds.add(itemId);
		}
	}

	public void deleteItemIds(List<String> deleteItemIds) {
		Iterator<String> iterator = this.itemIds.iterator();
		while (iterator.hasNext()) {
			if (deleteItemIds.contains(iterator.next())) {
				iterator.remove();
			}
		}
	}

	public void deleteItemId(String deleteItemId) {
		this.itemIds.remove(deleteItemId);
	}

	public abstract boolean isMember(JavaClass javaClass);

	protected boolean containPlace() {
		if (itemIds.isEmpty()) {
			return false;
		} else {
			return CandidateUtil.containPlace(itemIds.iterator().next());
		}
	}

	public abstract ComponentConf clone() throws CloneNotSupportedException;

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
		final ComponentConf other = (ComponentConf) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}