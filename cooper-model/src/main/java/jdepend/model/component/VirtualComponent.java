package jdepend.model.component;

import java.util.Collection;
import java.util.List;

import jdepend.metadata.JavaPackage;
import jdepend.model.Component;
import jdepend.model.ComponentException;
import jdepend.model.JavaClassUnit;

/**
 * 一种组织类的方式，不改变Class所属组件的性质
 * 
 * 在 {@link MoveRelationTODOItem}中用于聚合相关的类 在 {@link Component}中用于计算内聚性
 * 
 * @author user
 * 
 */
public class VirtualComponent extends Component {

	private static final long serialVersionUID = -1281457261148140764L;

	public VirtualComponent() {
		super();
	}

	public VirtualComponent(String name) {
		super(name);
	}

	public VirtualComponent(JavaClassUnit javaClass) {
		super(javaClass.getName());
		this.joinJavaClass(javaClass);
		this.setResult(javaClass.getResult());
	}

	public VirtualComponent(Component component) {
		super(component.getName());
		for (JavaClassUnit javaClass : component.getClasses()) {
			this.joinJavaClass(javaClass);
		}
		this.setResult(component.getResult());
	}

	/**
	 * 建立组件与类之间的单向关联
	 * 
	 * @param javaClass
	 */
	public synchronized void joinJavaClass(JavaClassUnit javaClass) {
		if (!this.javaClasses.contains(javaClass)) {
			this.javaClasses.add(javaClass);
			this.javaClassesForId.put(javaClass.getId(), javaClass);
		}
	}

	@Override
	protected List<Component> doList(Collection<JavaPackage> javaPackages) throws ComponentException {
		throw new ComponentException("虚拟组件不进行组件的识别");
	}
}
