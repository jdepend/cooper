package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.Component;
import jdepend.model.JDependUnit;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassRelationItem;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.element.layer.JEELayer;
import jdepend.util.analyzer.element.layer.JavaClassType;
import jdepend.util.analyzer.element.layer.LayerInfo;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public class ActionFormOutWebLayer extends AbstractAnalyzer {

	private static final long serialVersionUID = -7231037234241760758L;

	private String ActionFormSuperClassName;

	private String ActionPackageEndsWith;
	private String ServicePackageEndsWith;
	private String DaoPackageEndsWith;

	private String ActionSuperClassName;
	private String ServiceSuperClassName;
	private String DaoSuperClassName;

	public ActionFormOutWebLayer() {
		super("ActionForm出现在web层以外", Analyzer.AntiPattern, "ActionForm出现在web层以外");

		if (this.ActionFormSuperClassName == null) {
			this.ActionFormSuperClassName = "org.apache.struts.action.ActionForm";
		}
		if (this.ActionPackageEndsWith == null) {
			this.ActionPackageEndsWith = ".action";
		}
		if (this.ServicePackageEndsWith == null) {
			this.ServicePackageEndsWith = ".service";
		}
		if (this.DaoPackageEndsWith == null) {
			this.DaoPackageEndsWith = ".dao";
		}
	}

	protected void doSearch(AnalysisResult result) throws JDependException {

		List<LayerInfo> layerInfos = new ArrayList<LayerInfo>();

		layerInfos.add(new LayerInfo("WebLayer", ActionSuperClassName, ActionPackageEndsWith));
		layerInfos.add(new LayerInfo("ServiceLayer", ServiceSuperClassName, ServicePackageEndsWith));
		layerInfos.add(new LayerInfo("DaoLayer", DaoSuperClassName, DaoPackageEndsWith));

		JEELayer jeeLayer = new JEELayer(layerInfos);

		List<Component> components = jeeLayer.list(result.getJavaPackages());

		List<String> supers = new ArrayList<String>();
		supers.add(ActionFormSuperClassName);
		JavaClassType ActionFormClassType = new JavaClassType("ActionForm", supers);
		ActionFormClassType.setJavaClasses(result.getClasses());

		JDependUnit serviceLayer = null;
		JDependUnit daoLayer = null;
		for (JDependUnit component : components) {
			if (component.getName().equals("ServiceLayer")) {
				serviceLayer = component;
			} else if (component.getName().equals("DaoLayer")) {
				daoLayer = component;
			}
		}

		for (JavaClass serviceClass : serviceLayer.getClasses()) {
			for (JavaClassRelationItem relationItem : serviceClass.getCeItems()) {
				if (ActionFormClassType.isMember(relationItem.getTarget())) {
					this.print(relationItem.getTarget().getName() + " into " + serviceClass.getName());
				}
			}
		}

		for (JavaClass daoClass : daoLayer.getClasses()) {
			for (JavaClassRelationItem relationItem : daoClass.getCeItems()) {
				if (ActionFormClassType.isMember(relationItem.getTarget())) {
					this.print(relationItem.getTarget().getName() + " into " + daoClass.getName());
				}
			}
		}

	}

	public void setActionFormSuperClassName(String actionFormSuperClassName) {
		ActionFormSuperClassName = actionFormSuperClassName;
	}

	public void setActionPackageEndsWith(String actionPackageEndsWith) {
		ActionPackageEndsWith = actionPackageEndsWith;
	}

	public void setServicePackageEndsWith(String servicePackageEndsWith) {
		ServicePackageEndsWith = servicePackageEndsWith;
	}

	public void setDaoPackageEndsWith(String daoPackageEndsWith) {
		DaoPackageEndsWith = daoPackageEndsWith;
	}

	public void setActionSuperClassName(String actionSuperClassName) {
		ActionSuperClassName = actionSuperClassName;
	}

	public void setServiceSuperClassName(String serviceSuperClassName) {
		ServiceSuperClassName = serviceSuperClassName;
	}

	public void setDaoSuperClassName(String daoSuperClassName) {
		DaoSuperClassName = daoSuperClassName;
	}

	public String getActionFormSuperClassName() {
		return ActionFormSuperClassName;
	}

	public String getActionPackageEndsWith() {
		return ActionPackageEndsWith;
	}

	public String getServicePackageEndsWith() {
		return ServicePackageEndsWith;
	}

	public String getDaoPackageEndsWith() {
		return DaoPackageEndsWith;
	}

	public String getActionSuperClassName() {
		return ActionSuperClassName;
	}

	public String getServiceSuperClassName() {
		return ServiceSuperClassName;
	}

	public String getDaoSuperClassName() {
		return DaoSuperClassName;
	}

}
