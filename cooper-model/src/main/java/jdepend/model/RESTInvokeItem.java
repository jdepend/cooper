package jdepend.model;

import jdepend.model.util.JavaClassCollection;

public final class RESTInvokeItem extends InvokeItem {

	private static final long serialVersionUID = -4196380179142091486L;

	private String url;

	private String constantClassName;
	private String constantAttributeName;

	public RESTInvokeItem(String url, String constantClassName, String constantAttributeName) {
		super();
		this.url = url;
		this.constantClassName = constantClassName;
		this.constantAttributeName = constantAttributeName;
	}

	/**
	 * 补充method信息
	 * 
	 * @param javaClasses
	 * @return
	 */
	@Override
	public boolean supplyMethod(JavaClassCollection javaClasses) {

		if (this.constantClassName != null && this.constantAttributeName != null) {
			JavaClass urlClass = javaClasses.getTheClass(this.getSelf().getJavaClass().getPlace(),
					this.constantClassName);
			if (urlClass != null) {
				L: for (Attribute attribute : urlClass.getAttributes()) {
					if (attribute.getStaticValue() != null && attribute.getName().equals(this.constantAttributeName)) {
						url = attribute.getStaticValue();
						break L;
					}
				}
			}
		}

		for (JavaClass javaClass : javaClasses.getJavaClasses()) {
			if (javaClass.getDetail().getRequestMapping() != null) {
				for (Method method : javaClass.getMethods()) {
					if (this.math2(method)) {
						this.setMethod(method);
						return true;
					}
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
		return method.getRequestMappingValue() != null && this.url != null
				&& method.getRequestMappingValue().startsWith(this.url);
	}

	@Override
	public int hashCode() {
		if (this.getMethod() != null) {
			final int prime = 31;
			int result = 1;
			result = prime * result + getMethod().hashCode();
			return result;
		} else {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((url == null) ? 0 : url.hashCode());

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
		RESTInvokeItem other = (RESTInvokeItem) obj;
		if (this.getMethod() != null) {
			if (other.getMethod() == null) {
				return false;
			} else {
				return this.getMethod().equals(other.getMethod());
			}
		} else {
			if (url == null) {
				if (other.url != null)
					return false;
			} else if (!url.equals(other.url))
				return false;

			return true;
		}
	}

	@Override
	public String toString() {
		if (this.getMethod() != null) {
			return "InvokeItem [invokeClassName=" + getMethod().getJavaClass().getName() + ", invokeMethodName="
					+ getMethod().getName() + ", invokeMethodSignature=" + getMethod().getSignature() + "]";
		} else {
			return "InvokeItem [url=" + url + "]";
		}
	}
}
