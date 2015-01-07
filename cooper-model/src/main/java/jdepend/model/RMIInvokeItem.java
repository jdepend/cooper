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

		this.self = item.self;
		this.method = item.method;
	}

	/**
	 * 补充method信息
	 * 
	 * @param javaClasses
	 * @return
	 */
	@Override
	public boolean supplyMethod(JavaClassCollection javaClasses) {
		JavaClass invokeClass = javaClasses.getTheClass(invokeClassPlace, invokeClassName);
		if (invokeClass != null) {
			for (Method invokeMethod : invokeClass.getMethods()) {
				if (this.math2(invokeMethod)) {
					this.setMethod(invokeMethod);
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
	public int hashCode() {
		if (this.getMethod() != null && this.getSelf() != null) {
			final int prime = 31;
			int result = 1;
			result = prime * result + getMethod().hashCode();
			result = prime * result + getSelf().hashCode();
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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RMIInvokeItem other = (RMIInvokeItem) obj;
		if (this.getMethod() != null && this.getSelf() != null) {
			if (other.getMethod() == null || other.getSelf() == null) {
				return false;
			} else {
				return this.getSelf().equals(other.getSelf()) && this.getMethod().equals(other.getMethod());
			}
		} else {
			if (invokeClassName == null) {
				if (other.invokeClassName != null)
					return false;
			} else if (!invokeClassName.equals(other.invokeClassName))
				return false;
			if (invokeMethodName == null) {
				if (other.invokeMethodName != null)
					return false;
			} else if (!invokeMethodName.equals(other.invokeMethodName))
				return false;
			if (invokeMethodSignature == null) {
				if (other.invokeMethodSignature != null)
					return false;
			} else if (!invokeMethodSignature.equals(other.invokeMethodSignature))
				return false;
			return true;
		}
	}

	@Override
	public String toString() {
		if (this.getMethod() != null) {
			return "InvokeItem [type=RMIInvokeItem, invokeClassName=" + getMethod().getJavaClass().getName()
					+ ", invokeMethodName=" + getMethod().getName() + ", invokeMethodSignature="
					+ getMethod().getSignature() + "]";
		} else {
			return "InvokeItem [type=RMIInvokeItem, invokeClassName=" + invokeClassName + ", invokeMethodName="
					+ invokeMethodName + ", invokeMethodSignature=" + invokeMethodSignature + "]";
		}
	}
}
