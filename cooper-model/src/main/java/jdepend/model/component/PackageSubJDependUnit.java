package jdepend.model.component;

import jdepend.model.GroupInfoCalculator;
import jdepend.model.JavaClassUnit;
import jdepend.model.JavaPackage;

/**
 * 以包为单位用于计算组件内聚性的二级元素
 * 
 * @author user
 *
 */
public class PackageSubJDependUnit extends VirtualComponent {

	public PackageSubJDependUnit(JavaPackage javaPackage) {
		super(javaPackage.getName());

		for (JavaClassUnit javaClass : javaPackage.getClasses()) {
			this.joinJavaClass(javaClass);
			if (this.getResult() == null) {
				this.setResult(javaClass.getResult());
			}
		}
	}

	public JavaPackage getJavaPackage() {
		return this.getJavaPackages().iterator().next();
	}

	@Override
	public float getCohesion() {
		return this.getGroupCohesionInfo().getCohesion();
	}

	@Override
	public float getBalance() {
		return this.getGroupInfoCalculator().getBalance();
	}

	@Override
	protected GroupInfoCalculator createGroupInfoCalculator() {
		return new GroupInfoCalculator(this);
	}
}
