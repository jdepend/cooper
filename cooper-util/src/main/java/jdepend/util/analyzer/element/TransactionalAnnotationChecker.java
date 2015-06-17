package jdepend.util.analyzer.element;

import jdepend.metadata.Method;
import jdepend.model.JavaClassUnit;
import jdepend.model.result.AnalysisResult;
import jdepend.util.analyzer.framework.AbstractAnalyzer;
import jdepend.util.analyzer.framework.Analyzer;
import jdepend.util.analyzer.framework.AnalyzerException;

public final class TransactionalAnnotationChecker extends AbstractAnalyzer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3741562066847145590L;

	private String daoEndClassName;
	private String serviceEndClassName;
	private String facadeEndClassName;
	private String controllerEndClassName;

	public TransactionalAnnotationChecker() {
		super("搜索不合理的事务注解", Analyzer.AntiPattern, "搜索不合理的事务注解");
	}

	@Override
	protected void doSearch(AnalysisResult result) throws AnalyzerException {
		if (controllerEndClassName != null && controllerEndClassName.length() > 0) {
			this.print("Controller方法上标注了事务注解：\n");
			L1: for (JavaClassUnit javaClass : result.getClasses()) {
				if (javaClass.getName().endsWith(controllerEndClassName)) {
					if (!javaClass.getJavaClass().isInterface()) {
						if (javaClass.getJavaClass().getTransactional() != null) {
							this.print(javaClass.getName());
							this.print("\n");
							break L1;
						}
						for (Method method : javaClass.getJavaClass().getSelfMethods()) {
							if (method.getTransactional() != null && !method.isConstruction()) {
								this.print(javaClass.getName() + "." + method.getName());
								this.print("\n");
							}
						}
					}
				}
			}
			this.print("\n");
		}
		if (facadeEndClassName != null && facadeEndClassName.length() > 0) {
			this.print("Facade方法上沒有标注事务注解：\n");
			L2: for (JavaClassUnit javaClass : result.getClasses()) {
				if (javaClass.getName().endsWith(facadeEndClassName)) {
					if (!javaClass.getJavaClass().isInterface()) {
						if (javaClass.getJavaClass().getTransactional() != null) {
							break L2;
						}
						for (Method method : javaClass.getJavaClass().getSelfMethods()) {
							if (method.getTransactional() == null && method.isPublic() && !method.isConstruction()) {
								this.print(javaClass.getName() + "." + method.getName());
								this.print("\n");
							}
						}
					}
				}
			}
			this.print("\n");
		}
		if (serviceEndClassName != null && serviceEndClassName.length() > 0) {
			this.print("Service方法上沒有标注事务注解：\n");
			L3: for (JavaClassUnit javaClass : result.getClasses()) {
				if (javaClass.getName().endsWith(serviceEndClassName)) {
					if (!javaClass.getJavaClass().isInterface()) {
						if (javaClass.getJavaClass().getTransactional() != null) {
							break L3;
						}
						for (Method method : javaClass.getJavaClass().getSelfMethods()) {
							if (method.getTransactional() == null && method.isPublic() && !method.isConstruction()) {
								this.print(javaClass.getName() + "." + method.getName());
								this.print("\n");
							}
						}
					}
				}
			}
			this.print("\n");
		}
		if (daoEndClassName != null && daoEndClassName.length() > 0) {
			this.print("DAO方法上标注了事务注解：\n");
			L4: for (JavaClassUnit javaClass : result.getClasses()) {
				if (javaClass.getName().endsWith(daoEndClassName)) {
					if (!javaClass.getJavaClass().isInterface()) {
						if (javaClass.getJavaClass().getTransactional() != null) {
							this.print(javaClass.getName());
							this.print("\n");
							break L4;
						}
						for (Method method : javaClass.getJavaClass().getSelfMethods()) {
							if (method.getTransactional() != null && !method.isConstruction()) {
								this.print(javaClass.getName() + "." + method.getName());
								this.print("\n");
							}
						}
					}
				}
			}
			this.print("\n");
		}

	}

	public String getDaoEndClassName() {
		return daoEndClassName;
	}

	public void setDaoEndClassName(String daoEndClassName) {
		this.daoEndClassName = daoEndClassName;
	}

	public String getServiceEndClassName() {
		return serviceEndClassName;
	}

	public void setServiceEndClassName(String serviceEndClassName) {
		this.serviceEndClassName = serviceEndClassName;
	}

	public String getFacadeEndClassName() {
		return facadeEndClassName;
	}

	public void setFacadeEndClassName(String facadeEndClassName) {
		this.facadeEndClassName = facadeEndClassName;
	}

	public String getControllerEndClassName() {
		return controllerEndClassName;
	}

	public void setControllerEndClassName(String controllerEndClassName) {
		this.controllerEndClassName = controllerEndClassName;
	}

}
