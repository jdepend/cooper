package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.List;

import jdepend.metadata.JavaClassRelationItem;
import jdepend.model.Component;
import jdepend.model.ComponentException;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.element.layer.JEELayer;
import jdepend.util.analyzer.element.layer.JavaClassType;
import jdepend.util.analyzer.element.layer.LayerInfo;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.AnalyzerException;

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

	protected void doExecute(AnalysisResult result) throws AnalyzerException {

		List<LayerInfo> layerInfos = new ArrayList<LayerInfo>();

		layerInfos.add(new LayerInfo("DaoLayer", DaoSuperClassName, DaoPackageEndsWith));

		JEELayer jeeLayer = new JEELayer(layerInfos);

		List<Component> components;
		try {
			components = jeeLayer.list(result.getJavaPackages());
		} catch (ComponentException e) {
			throw new AnalyzerException(e);
		}

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

		for (JavaClassUnit daoClass : daoLayer.getClasses()) {
			for (JavaClassRelationItem relationItem : daoClass.getJavaClass().getCeItems()) {
				if (ServiceClassType.isMember(relationItem.getTarget())) {
					this.print(daoClass.getName() + " Call " + relationItem.getTarget().getName());
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
