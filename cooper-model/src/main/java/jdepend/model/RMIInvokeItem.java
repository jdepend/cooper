package jdepend.model;

import jdepend.model.util.JavaClassCollection;
import jdepend.model.util.ParseUtil;

public final class RMIInvokeItem extends RemoteInvokeItem {

	private static final long serialVersionUID = 8888171995972886018L;

	private String invokeClassPlace;
	private String invokeClassName;
	private String invokeMethodName;
	private String invokeMethodSignature;

	public RMIInvokeItem(LocalInvokeItem item) {
		super();
		this.invokeClassPlace = item.getInvokeClassPlace();
		this.invokeClassName = item.getInvokeClassName();
		this.invokeMethodName = item.getInvokeMethodName();
		this.invokeMethodSignature = item.getInvokeMethodSignature();

		this.caller = item.caller;
		this.callee = item.callee;
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
	public String getName() {
		return "RMI";
	}

	@Override
	public int hashCode() {
		if (this.getCallee() != null && this.getCaller() != null) {
			final int prime = 31;
			int result = 1;
			result = prime * result + getCallee().hashCode();
			result = prime * result + getCaller().hashCode();
			return result;
		} else {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((invokeClassName == null) ? 0 : invokeClassName.hashCode());
			result = prime * result + ((invokeMethodName == null) ? 0 : invokeMethodName.hashCode());
			result = prime * result + ((invokeMethodSignature == null) ? 0 : invokeMethodSignature.hashCode());
			return result;
		}
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		RMIInvokeItem other = (RMIInvokeItem) obj;
		if (this.getCallee() != null && this.getCaller() != null) {
			return this.getCaller().equals(other.getCaller()) && this.getCallee().equals(other.getCallee());
		} else {
			if (!invokeClassName.equals(other.invokeClassName))
				return false;
			if (!invokeMethodName.equals(other.invokeMethodName))
				return false;
			if (!invokeMethodSignature.equals(other.invokeMethodSignature))
				return false;
			return true;
		}
	}

	@Override
	public String toString() {
		if (this.getCallee() != null) {
			return "InvokeItem [type=RMIInvokeItem, invokeClassName=" + getCallee().getJavaClass().getName()
					+ ", invokeMethodName=" + getCallee().getName() + ", invokeMethodSignature="
					+ getCallee().getSignature() + "]";
		} else {
			return "InvokeItem [type=RMIInvokeItem, invokeClassName=" + invokeClassName + ", invokeMethodName="
					+ invokeMethodName + ", invokeMethodSignature=" + invokeMethodSignature + "]";
		}
	}
}
