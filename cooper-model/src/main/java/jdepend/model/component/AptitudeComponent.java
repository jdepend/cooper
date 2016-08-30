package jdepend.model.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaPackage;
import jdepend.metadata.tree.JavaPackageNode;
import jdepend.metadata.tree.JavaPackageTreeCreator;
import jdepend.model.Component;
import jdepend.model.ComponentException;
import jdepend.model.JavaClassUnit;
import jdepend.model.component.judge.ComponentJudge;
import jdepend.model.component.judge.ComponentJudgeFactory;

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
	public void init(String group, String command, String info) throws ComponentException {
		this.initJudges(group, command);
	}

	@Override
	protected List<Component> doList(Collection<JavaPackage> javaPackages) throws ComponentException {
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
			throws ComponentException {
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
				for (JavaClass javaClass : javaPackage.getClasses()) {
					component.addJavaClass(new JavaClassUnit(javaClass));
				}
			}
		}
		return component;
	}

	private boolean isComponent(JavaPackageNode node) throws ComponentException {

		for (ComponentJudge judge : this.judges) {
			if (judge.isComponent(node)) {
				return true;
			}
		}
		return false;
	}

	private void initJudges(String group, String command) throws ComponentException {

		if (group == null || command == null) {
			throw new ComponentException("执行环境中没有RunningCommand信息。");
		}

		this.judges = (new ComponentJudgeFactory(group, command)).getJudges();
	}

	public void addJudge(ComponentJudge judge) {
		if (!this.judges.contains(judge)) {
			this.judges.add(judge);
		}
	}
}
