package jdepend.util.analyzer.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClass;
import jdepend.model.JavaClassType;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;

public class ServiceHaveState extends AbstractAnalyzer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4998893503475931338L;

	private String endWith;

	private String ServiceSuperClassName;

	public ServiceHaveState() {
		super("Service中存在属性", Analyzer.AntiPattern, "Service中存在属性");
		this.setType(AntiPattern);
	}

	protected void doSearch(AnalysisResult result) throws JDependException {

		List<String> supers = new ArrayList<String>();
		supers.add(ServiceSuperClassName);
		JavaClassType serviceClassType = new JavaClassType("Service", supers, this.endWith);

		Collection<JavaClass> javaClasses = result.getClasses();
		serviceClassType.setJavaClasses(javaClasses);

		int totalService = 0;
		int printService = 0;
		for (JavaClass javaClass : javaClasses) {
			if (serviceClassType.isMember(javaClass)) {
				totalService++;
				if (javaClass.isState()) {
					this.print(javaClass.getName() + "\n");
					printService++;
				}
			}
		}
		this.print("\n");
		this.print("TotalServices : " + totalService);
		this.print("PrintServices : " + printService);
		this.print("\n");

	}

	public String getEndWith() {
		return endWith;
	}

	public void setEndWith(String endWith) {
		this.endWith = endWith;
	}

	public String getServiceSuperClassName() {
		return ServiceSuperClassName;
	}

	public void setServiceSuperClassName(String serviceSuperClassName) {
		ServiceSuperClassName = serviceSuperClassName;
	}

}
