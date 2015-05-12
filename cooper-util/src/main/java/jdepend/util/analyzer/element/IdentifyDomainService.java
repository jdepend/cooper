package jdepend.util.analyzer.element;

import java.util.Collection;
import java.util.HashSet;

import jdepend.framework.exception.JDependException;
import jdepend.model.Attribute;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public class IdentifyDomainService extends AbstractAnalyzer {

	private static final long serialVersionUID = -8083874482518842003L;

	private String serviceClassName;

	private transient Collection<String> serviceNames;

	public IdentifyDomainService() {
		super("识别DomainService", Analyzer.Attention, "识别DomainService");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws JDependException {

		int count;
		boolean domain;
		for (JavaClassUnit javaClass : result.getClasses()) {
			if (isService(javaClass)) {
				domain = true;
				count = 0;
				L: for (Attribute attribute : javaClass.getAttributes()) {
					for (JavaClassUnit type : attribute.getTypeClasses()) {
						if (isService(type)) {
							if (!type.containedComponent() || !javaClass.getComponent().equals(type.getComponent())) {
								domain = false;
								break L;
							} else {
								count++;
								if (count > 1) {
									domain = false;
									break L;
								}
							}
						}
					}
				}
				if (domain) {
					this.printTable("Service名", javaClass.getName());
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

	private boolean isService(JavaClassUnit javaClass) {
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
		explain.append("识别具有原子性质的Service，规则：<br>");
		explain.append("1、该类名以Facade、Service、BO、Stub結尾。<br>");
		explain.append("2、该类没有注入了其他组件的Facade或Service。<br>");
		explain.append("3、该类仅注入了本组件零个或一个其他Service。<br>");
		return explain.toString();
	}
}
