package jdepend.model.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JavaClassUnit;
import jdepend.model.JavaPackage;
import jdepend.model.component.judge.ComponentJudge;
import jdepend.model.component.judge.ComponentJudgeFactory;
import jdepend.model.component.judge.WisdomLayerComponentJudge;
import jdepend.model.tree.JavaPackageNode;
import jdepend.model.tree.JavaPackageTreeCreator;

/**
 * 按规则识别组件
 * 
 * @author ibmuser
 * 
 */
public final class AptitudeComponent extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8034734675779292126L;

	private List<ComponentJudge> judges = new ArrayList<ComponentJudge>();

	public AptitudeComponent() {
		super();
	}

	public AptitudeComponent(String name) {
		super(name);
	}

	@Override
	public void init(String group, String command, String info) throws JDependException {
		this.initJudges(group, command);
	}

	@Override
	protected List<Component> doList(Collection<JavaPackage> javaPackages) throws JDependException {
		// 清空临时计算结果
		List<Component> components = new ArrayList<Component>();
		// 构造JavaPackageTree
		JavaPackageNode root = (new JavaPackageTreeCreator()).createTree(javaPackages);
		// 创建组件
		if (this.isComponent(root)) {
			components.add(this.createComponent(root.getPath(), javaPackages));
		} else {
			this.createComponent(root, javaPackages, components);
		}

		return components;
	}

	private void createComponent(JavaPackageNode node, Collection<JavaPackage> javaPackages, List<Component> components)
			throws JDependException {
		for (JavaPackageNode child : node.getChildren()) {
			if (this.isComponent(child)) {
				components.add(this.createComponent(child.getPath(), javaPackages));
			} else {
				createComponent(child, javaPackages, components);
			}
		}
	}

	private Component createComponent(String name, Collection<JavaPackage> javaPackages) {
		AptitudeComponent component = new AptitudeComponent(name);
		for (JavaPackage javaPackage : javaPackages) {
			if (javaPackage.getName().startsWith(name)) {
				for (JavaClassUnit javaClass : javaPackage.getClasses()) {
					component.addJavaClass(javaClass);
				}
			}
		}
		return component;
	}

	private boolean isComponent(JavaPackageNode node) throws JDependException {

		for (ComponentJudge judge : this.judges) {
			if (judge.isComponent(node)) {
				return true;
			}
		}
		return false;
	}

	private void initJudges(String group, String command) throws JDependException {

		if (group == null || command == null) {
			throw new JDependException("执行环境中没有RunningCommand信息。");
		}

		this.judges = (new ComponentJudgeFactory(group, command)).getJudges();

		if (this.judges == null || this.judges.size() == 0) {
			this.addJudge(new WisdomLayerComponentJudge());
		}
	}

	public void addJudge(ComponentJudge judge) {
		if (!this.judges.contains(judge)) {
			this.judges.add(judge);
		}
	}
}
