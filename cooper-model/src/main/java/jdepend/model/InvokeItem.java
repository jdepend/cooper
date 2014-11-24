package jdepend.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import jdepend.model.component.modelconf.CandidateUtil;
import jdepend.model.util.ParseUtil;

public final class InvokeItem implements Serializable {

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

	private transient Method self;
	private transient Method method;

	public InvokeItem(String invokeType, String invokeClassPlace, String invokeClassName, String invokeMethodName,
			String invokeMethodSignature) {
		super();
		this.invokeType = invokeType;
		this.invokeClassPlace = invokeClassPlace;
		this.invokeClassName = invokeClassName;
		this.invokeMethodName = invokeMethodName;
		this.invokeMethodSignature = invokeMethodSignature;
	}

	public Method getMethod() {
		return method;
	}

	public void setSelf(Method self) {
		this.self = self;
	}

	public String getInvokeClassName() {
		return this.invokeClassName;
	}

	public String getInvokeClassPlace() {
		return invokeClassPlace;
	}

	public void setInvokeClassPlace(String invokeClassPlace) {
		this.invokeClassPlace = invokeClassPlace;
	}

	/**
	 * 补充method信息
	 * 
	 * @param javaClasses
	 * @return
	 */
	public boolean supplyMethod(Map<String, JavaClass> javaClasses) {
		JavaClass invokeClass = javaClasses.get(CandidateUtil.getId(this.getInvokeClassPlace(),
				this.getInvokeClassName()));
		if (invokeClass != null) {
			for (Method invokeMethod : invokeClass.getMethods()) {
				if (this.math2(invokeMethod)) {
					this.method = invokeMethod;
					this.method.addInvokedMethod(self);
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
		if (this.method != null) {
			final int prime = 31;
			int result = 1;
			result = prime * result + method.hashCode();
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
		InvokeItem other = (InvokeItem) obj;
		if (this.method != null) {
			if (other.method == null) {
				return false;
			} else {
				return this.method.equals(other.method);
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
		if (this.method != null) {
			return "InvokeItem [invokeClassName=" + method.getJavaClass().getName() + ", invokeMethodName="
					+ method.getName() + ", invokeMethodSignature=" + method.getSignature() + "]";
		} else {
			return "InvokeItem [invokeClassName=" + invokeClassName + ", invokeType=" + invokeType
					+ ", invokeMethodName=" + invokeMethodName + ", invokeMethodSignature=" + invokeMethodSignature
					+ "]";
		}
	}
}
