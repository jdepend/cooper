package jdepend.model.component;

import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaPackage;
import jdepend.model.GroupInfoCalculator;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;

/**
 * 以包为单位用于计算组件内聚性的二级元素
 * 
 * @author user
 * 
 */
public class PackageSubJDependUnit extends VirtualComponent {

	public PackageSubJDependUnit(JavaPackage javaPackage, AnalysisResult result) {
		super(javaPackage.getName(), result);

		for (JavaClass javaClass : javaPackage.getClasses()) {
			JavaClassUnit javaClassUnit = result.getTheClass(javaClass.getId());
			this.joinJavaClass(javaClassUnit);
			if (this.getResult() == null) {
				this.setResult(javaClassUnit.getResult());
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
	public Float getBalance() {
		return this.getGroupInfoCalculator().getBalance();
	}

	@Override
	protected GroupInfoCalculator createGroupInfoCalculator() {
		return new GroupInfoCalculator(this);
	}
}
