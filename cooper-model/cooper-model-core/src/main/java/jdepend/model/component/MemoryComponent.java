package jdepend.model.component;

import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.metadata.JavaPackage;
import jdepend.model.Component;
import jdepend.model.ComponentException;

public final class MemoryComponent extends Component {

	private static final long serialVersionUID = -4893803692397356980L;

	public MemoryComponent() {
		super();
	}

	public MemoryComponent(String name) {
		super(name);
	}

	@Override
	protected List<Component> doList(Collection<JavaPackage> javaPackages) throws ComponentException {
		throw new ComponentException("空组件不进行组件的识别");
	}

	public void setAfferents(Collection<Component> units) {
		this.afferents = units;
	}

	public void setEfferents(Collection<Component> units) {
		this.efferents = units;

	}

}
