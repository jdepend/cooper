package jdepend.model.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jdepend.framework.exception.JDependException;
import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaPackage;
import jdepend.model.Component;
import jdepend.model.ComponentException;
import jdepend.model.JavaClassUnit;
import jdepend.model.component.modelconf.ComponentConf;
import jdepend.model.component.modelconf.ComponentModelConf;
import jdepend.model.component.modelconf.ComponentModelConfMgr;

/**
 * 定制组件
 * 
 * @author wangdg
 * 
 */
public final class CustomComponent extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4611204363465134237L;

	private ComponentModelConf componentModelConf;

	public CustomComponent() {
	}

	public CustomComponent(String componentName) {
		super(componentName);
	}

	@Override
	public void init(String group, String command, String info) throws ComponentException {
		try {
			componentModelConf = ComponentModelConfMgr.getInstance().getTheComponentModelConf(group, info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("CustomComponent 初始化失败！");
		}
	}

	@Override
	protected List<Component> doList(Collection<JavaPackage> javaPackages) throws ComponentException {

		if (componentModelConf == null || componentModelConf.size() == 0) {
			throw new ComponentException("没有定义组件信息！");
		}

		List<Component> components = new ArrayList<Component>();
		CustomComponent component;

		for (ComponentConf componentConf : componentModelConf.getComponentConfs()) {
			component = new CustomComponent(componentConf.getName());
			// 设置Layer
			component.setLayer(componentConf.getLayer());

			for (JavaPackage javaPackage : javaPackages) {
				for (JavaClass javaClass : javaPackage.getClasses()) {
					if (componentConf.isMember(javaClass)) {
						component.addJavaClass(new JavaClassUnit(javaClass));
					}
				}
			}

			components.add(component);
		}
		return components;
	}

	@Override
	public Component clone(Map<String, JavaClassUnit> javaClasses) throws ComponentException {

		CustomComponent obj = (CustomComponent) super.clone(javaClasses);
		obj.setComponentInfo(this.componentModelConf);

		return obj;
	}

	public void setComponentInfo(ComponentModelConf componentInfo) {
		this.componentModelConf = componentInfo;
	}

	public ComponentModelConf getComponentModelConf() {
		return componentModelConf;
	}
}
