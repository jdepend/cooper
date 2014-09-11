package jdepend.model.component.modelconf;

import java.io.Serializable;

import jdepend.model.JavaClass;

/**
 * 组件模型下一个组件的配置信息
 * 
 * @author wangdg
 * 
 */
public abstract class ComponentConf implements Serializable, Cloneable {

	private String name;
	private int layer;

	public ComponentConf(String name) {
		super();
		this.name = name;
	}

	public ComponentConf(String name, int layer) {
		super();
		this.name = name;
		this.layer = layer;
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
	
	public abstract boolean isMember(JavaClass javaClass); 

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