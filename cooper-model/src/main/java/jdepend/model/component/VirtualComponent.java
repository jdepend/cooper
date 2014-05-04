package jdepend.model.component;

import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JavaClass;
import jdepend.model.JavaPackage;

/**
 * 一种组织类的方式，不改变Class所属组件的性质
 * 
 * 在 {@link MoveRelationTODOItem}中用于聚合相关的类
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
	
	public VirtualComponent(JavaClass javaClass) {
		super(javaClass.getName());
		this.addJavaClass(javaClass);
	}

	public VirtualComponent(Component component) {
		super(component.getName());
		for (JavaClass javaClass : component.getClasses()) {
			this.addJavaClass(javaClass);
		}
	}

	@Override
	protected List<Component> doList(Collection<JavaPackage> javaPackages) throws JDependException {
		throw new JDependException("虚拟组件不进行组件的识别");
	}

	@Override
	public void addJavaClass(JavaClass javaClass) {
		if (!this.getClasses().contains(javaClass)) {
			this.getClasses().add(javaClass);
		}
	}

}
