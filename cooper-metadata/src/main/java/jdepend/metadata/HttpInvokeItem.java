package jdepend.metadata;

import java.util.Map;

import jdepend.metadata.util.JavaClassCollection;

public final class HttpInvokeItem extends RemoteInvokeItem {

	private static final long serialVersionUID = -4196380179142091486L;

	private String url;

	private transient String constantClassName;
	private transient String constantAttributeName;

	public HttpInvokeItem(String url, String constantClassName, String constantAttributeName) {
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
	public boolean supplyCallee(JavaClassCollection javaClasses) {

		Map<String, Method> httpMethods = javaClasses.getHttpMethod();
		if (httpMethods.size() == 0) {
			return false;
		}

		this.calUrl(javaClasses);

		if (url == null) {
			return false;
		}

		String mathUrl = this.formatUrl(this.url);
		for (String calledUrl : httpMethods.keySet()) {
			if (calledUrl.startsWith(mathUrl) || mathUrl.startsWith(calledUrl)) {
				this.setCallee(httpMethods.get(calledUrl));
				this.getCaller().getJavaClass().getDetail().setHttpCaller(true);
				return true;
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
		if (method.getRequestMapping() == null || this.url == null) {
			return false;
		} else {
			String mathUrl = this.formatUrl(this.url);
			String calledUrl = method.getRequestMappingValueNoVariable();

			if (calledUrl.startsWith(mathUrl) || mathUrl.startsWith(calledUrl)) {
				return true;
			} else {
				return false;
			}
		}
	}

	private void calUrl(JavaClassCollection javaClasses) {
		if (this.constantClassName != null && this.constantAttributeName != null) {
			JavaClass urlClass = javaClasses.getTheClass(this.getCaller().getJavaClass().getPlace(),
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
	}

	private String formatUrl(String url) {
		if (url.startsWith("\"") && url.endsWith("\"")) {
			return url.substring(1, url.length() - 1);
		} else {
			return url;
		}
	}

	@Override
	public String getName() {
		return "Http";
	}

	@Override
	public String toString() {
		if (this.getCallee() != null) {
			return "InvokeItem [type=HttpInvokeItem, invokeClassName=" + getCallee().getJavaClass().getName()
					+ ", invokeMethodName=" + getCallee().getName() + ", invokeMethodSignature="
					+ getCallee().getSignature() + "]";
		} else {
			StringBuilder info = new StringBuilder();
			info.append("InvokeItem [type=HttpInvokeItem, ");
			if (this.url != null) {
				info.append("url=" + url + ",");
			}
			if (this.constantClassName != null) {
				info.append("constantClassName=" + constantClassName + ",");
			}
			if (this.constantAttributeName != null) {
				info.append("constantAttributeName=" + constantAttributeName + ",");
			}
			info.delete(info.length() - 1, info.length());
			info.append("]");
			return info.toString();
		}
	}
}
