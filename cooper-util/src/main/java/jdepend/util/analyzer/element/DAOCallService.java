package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.JavaClassType;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.element.layer.JEELayer;
import jdepend.util.analyzer.element.layer.LayerInfo;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public class DAOCallService extends AbstractAnalyzer {

	private static final long serialVersionUID = 5980835716758700893L;

	private String DaoPackageEndsWith;

	private String ServiceSuperClassName;
	private String DaoSuperClassName;

	public DAOCallService() {
		super("DAO调用Service", Analyzer.AntiPattern, "DAO调用Service");
		this.setType(AntiPattern);

		if (this.DaoPackageEndsWith == null) {
			this.DaoPackageEndsWith = ".dao";
		}

	}

	protected void doSearch(AnalysisResult result) throws JDependException {

		List<LayerInfo> layerInfos = new ArrayList<LayerInfo>();

		layerInfos.add(new LayerInfo("DaoLayer", DaoSuperClassName, DaoPackageEndsWith));

		JEELayer jeeLayer = new JEELayer(layerInfos);

		List<Component> components = jeeLayer.list(result.getJavaPackages());

		List<String> supers = new ArrayList<String>();
		supers.add(ServiceSuperClassName);
		JavaClassType ServiceClassType = new JavaClassType("Service", supers);
		ServiceClassType.setJavaClasses(result.getClasses());

		JDependUnit daoLayer = null;
		for (JDependUnit component : components) {
			if (component.getName().equals("DaoLayer")) {
				daoLayer = component;
			}
		}

		for (JavaClass daoClass : daoLayer.getClasses()) {
			for (JavaClassRelationItem relationItem : daoClass.getCeItems()) {
				if (ServiceClassType.isMember(relationItem.getDepend())) {
					this.print(daoClass.getName() + " Call " + relationItem.getDepend().getName());
				}
			}
		}
	}

	public String getDaoPackageEndsWith() {
		return DaoPackageEndsWith;
	}

	public void setDaoPackageEndsWith(String daoPackageEndsWith) {
		DaoPackageEndsWith = daoPackageEndsWith;
	}

	public String getServiceSuperClassName() {
		return ServiceSuperClassName;
	}

	public void setServiceSuperClassName(String serviceSuperClassName) {
		ServiceSuperClassName = serviceSuperClassName;
	}

	public String getDaoSuperClassName() {
		return DaoSuperClassName;
	}

	public void setDaoSuperClassName(String daoSuperClassName) {
		DaoSuperClassName = daoSuperClassName;
	}
}
