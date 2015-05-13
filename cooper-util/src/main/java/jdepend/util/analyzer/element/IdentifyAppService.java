package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import jdepend.framework.exception.JDependException;
import jdepend.model.Attribute;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.model.util.JavaClassUnitUtil;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public class IdentifyAppService extends AbstractAnalyzer {

	private static final long serialVersionUID = -8083874482518842003L;

	private String serviceClassName;

	private transient Collection<String> serviceNames;

	public IdentifyAppService() {
		super("识别AppService", Analyzer.Attention, "识别AppService");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {

		Collection<String> attributes;
		for (JavaClassUnit javaClass : result.getClasses()) {
			if (isService(javaClass.getJavaClass())) {
				attributes = new ArrayList<String>();
				L: for (Attribute attribute : javaClass.getJavaClass().getAttributes()) {
					for (JavaClass type : attribute.getTypeClasses()) {
						if (isService(type)) {
							JavaClassUnit typeUnit = JavaClassUnitUtil.getJavaClassUnit(type);
							if (!typeUnit.containedComponent()
									|| !javaClass.getComponent().equals(typeUnit.getComponent())) {
								this.printTable("Service名", javaClass.getName());
								this.printTable("Attribute名", type.getName());
								break L;
							} else {
								attributes.add(type.getName());
								if (attributes.size() > 1) {
									for (String attr : attributes) {
										this.printTable("Service名", javaClass.getName());
										this.printTable("Attribute名", attr);
									}
									break L;
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void init() throws JDependException {

		serviceNames = new HashSet<String>();
		serviceNames.add("BO");
		serviceNames.add("BOImpl");
		serviceNames.add("Service");
		serviceNames.add("ServiceImpl");
		serviceNames.add("Facade");
		serviceNames.add("FacadeImpl");
		serviceNames.add("Stub");
		serviceNames.add("StubImpl");

		if (serviceClassName != null) {
			for (String name : this.serviceClassName.split(",")) {
				if (name != null && name.length() > 0) {
					serviceNames.add(name);
				}
			}
		}
	}

	private boolean isService(JavaClass javaClass) {
		for (String serviceName : serviceNames) {
			if (javaClass.getName().endsWith(serviceName)) {
				return true;
			}
		}
		return false;
	}

	public String getServiceClassName() {
		return serviceClassName;
	}

	public void setServiceClassName(String serviceClassName) {
		this.serviceClassName = serviceClassName;
	}

	@Override
	public String getExplain() {
		StringBuilder explain = new StringBuilder();
		explain.append("识别具有协调其他Service的Service，规则：<br>");
		explain.append("1、该类名以Facade、Service、BO、Stub結尾。<br>");
		explain.append("2、该类注入了其他组件的Facade或Service。<br>");
		explain.append("3、该类注入了本组件两个以上其他Service。<br>");
		return explain.toString();
	}
}
