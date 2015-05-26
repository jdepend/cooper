package jdepend.metadata;

import jdepend.metadata.util.JavaClassCollection;
import jdepend.metadata.util.ParseUtil;

public final class LocalInvokeItem extends InvokeItem {

	private static final long serialVersionUID = -5979020781021111806L;

	private String invokeType;
	private String invokeClassPlace;
	private String invokeClassName;
	private String invokeMethodName;
	private String invokeMethodSignature;

	private static final String specialType = "special";
	private static final String staticType = "static";
	private static final String virtualType = "virtual";
	private static final String interfaceType = "interface";

	public LocalInvokeItem(String invokeType, String invokeClassPlace, String invokeClassName, String invokeMethodName,
			String invokeMethodSignature) {
		super();
		this.invokeType = invokeType;
		this.invokeClassPlace = invokeClassPlace;
		this.invokeClassName = invokeClassName;
		this.invokeMethodName = invokeMethodName;
		this.invokeMethodSignature = invokeMethodSignature;
	}

	public String getInvokeClassPlace() {
		return invokeClassPlace;
	}

	public String getInvokeClassName() {
		return this.invokeClassName;
	}

	public String getInvokeMethodName() {
		return invokeMethodName;
	}

	public String getInvokeMethodSignature() {
		return invokeMethodSignature;
	}

	/**
	 * 补充method信息
	 * 
	 * @param javaClasses
	 * @return
	 */
	@Override
	public boolean supplyCallee(JavaClassCollection javaClasses) {
		JavaClass invokeClass = javaClasses.getTheClass(invokeClassPlace, invokeClassName);
		if (invokeClass != null) {
			for (Method invokeMethod : invokeClass.getMethods()) {
				if (this.math2(invokeMethod)) {
					this.setCallee(invokeMethod);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 调用的method是否是传入的method
	 * 
	 * @param method
	 * @return
	 */
	public boolean math2(Method method) {

		if (!this.invokeMethodName.equals(method.getName())) {
			return false;
		}
		if (this.invokeMethodSignature.equals(method.getSignature())) {
			return true;
		} else {
			String signature = method.getSignature();
			int start = signature.indexOf('<');
			if (start == -1) {
				return false;
			} else {
				String callSignature = ParseUtil.filterGenerics(signature);
				if (this.invokeMethodSignature.equals(callSignature)) {
					return true;
				} else {
					return false;
				}
			}
		}
	}

	@Override
	public InvokeItem transform() {
		if (this.getCallee().getJavaClass().getDetail().getInterfaceNames().contains("java.rmi.Remote")) {
			return new RMIInvokeItem(this);
		} else {
			return null;
		}
	}

	@Override
	public String getName() {
		return "Local";
	}

	@Override
	public String toString() {
		if (this.getCallee() != null) {
			return "InvokeItem [type=LocalInvokeItem, invokeClassName=" + getCallee().getJavaClass().getName()
					+ ", invokeMethodName=" + getCallee().getName() + ", invokeMethodSignature="
					+ getCallee().getSignature() + "]";
		} else {
			return "InvokeItem [type=LocalInvokeItem, invokeClassName=" + invokeClassName + ", invokeType="
					+ invokeType + ", invokeMethodName=" + invokeMethodName + ", invokeMethodSignature="
					+ invokeMethodSignature + "]";
		}
	}
}
