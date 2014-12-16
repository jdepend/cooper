package jdepend.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import jdepend.model.util.ParseUtil;
import jdepend.model.util.SignatureUtil;

import org.apache.bcel.classfile.AccessFlags;

public class Method extends AccessFlags {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8642392143295770051L;

	private String javaClassId;

	private String name;

	private String signature;

	private String info;

	private Collection<InvokeItem> invokeItems;

	private Collection<Attribute> readFields;

	private Collection<Attribute> writeFields;

	private int selfLineCount;

	private int argumentCount;

	private boolean isIncludeTransactionalAnnotation;

	private RequestMapping requestMapping;

	private transient Collection<String> argTypes;

	private transient Collection<String> returnTypes;

	private transient Collection<JavaClass> argClassTypes;

	private transient Collection<JavaClass> returnClassTypes;

	private transient Collection<Method> invokeMethods;

	private transient Collection<Method> invokedMethods;

	private transient JavaClass javaClass;

	public Method() {
	}

	public Method(JavaClass javaClass, org.apache.bcel.classfile.Method method) {
		this.javaClass = javaClass;
		this.javaClassId = javaClass.getId();
		this.access_flags = method.getAccessFlags();
		this.name = method.getName();
		this.signature = SignatureUtil.getSignature(method);
		this.info = ParseUtil.getMethodInfo(method.toString());
		this.argumentCount = this.calArgumentCount();
		this.invokeItems = new ArrayList<InvokeItem>();
		this.readFields = new ArrayList<Attribute>();
		this.writeFields = new ArrayList<Attribute>();
		this.isIncludeTransactionalAnnotation = false;
		this.selfLineCount = -1;
		this.invokedMethods = new HashSet<Method>();
	}

	public Method(String javaClassId, Method method) {

		this.javaClassId = javaClassId;
		this.access_flags = method.access_flags;
		this.name = method.name;
		this.signature = method.signature;
		this.info = method.info;
		this.argumentCount = method.argumentCount;
		this.invokedMethods = new HashSet<Method>();

		this.invokeItems = method.invokeItems;
		for (InvokeItem invokeItem : this.invokeItems) {
			invokeItem.setSelf(this);
			invokeItem.setMethod(null);
		}

		this.readFields = method.readFields;
		this.writeFields = method.writeFields;
		this.selfLineCount = method.selfLineCount;
		this.isIncludeTransactionalAnnotation = method.isIncludeTransactionalAnnotation;
		this.requestMapping = method.requestMapping;
	}

	public String getName() {
		return name;
	}

	public JavaClass getJavaClass() {
		return javaClass;
	}

	public void setJavaClass(JavaClass javaClass) {
		this.javaClass = javaClass;
	}

	public String getInfo() {
		return info;
	}

	public Collection<InvokeItem> getInvokeItems() {
		return invokeItems;
	}

	public Collection<Method> getInvokeMethods() {
		if (this.invokeMethods == null) {
			this.invokeMethods = new HashSet<Method>();
			for (InvokeItem item : this.invokeItems) {
				this.invokeMethods.add(item.getMethod());
			}
		}
		return this.invokeMethods;
	}

	public synchronized void addInvokedMethod(Method invokeMethod) {
		invokedMethods.add(invokeMethod);
	}

	public Collection<Method> getInvokedMethods() {
		if (this.invokedMethods == null) {
			invokedMethods = new HashSet<Method>();
			for (Method invokeMethod : this.getJavaClass().getResult().getMethods()) {
				if (!this.equals(invokeMethod) && this.isInvoked(invokeMethod)) {
					invokedMethods.add(invokeMethod);
				}
			}
		}
		return invokedMethods;
	}

	private boolean isInvoked(Method method) {

		if (method.getInvokeMethods().contains(this)) {
			return true;
		}

		for (Method overridedMethod : this.javaClass.getOverridedMethods(this)) {
			if (method.getInvokeMethods().contains(overridedMethod)) {
				return true;
			}
		}

		for (Method subOverrideMethod : this.javaClass.getSubOverrideMethods(this)) {
			if (method.getInvokeMethods().contains(subOverrideMethod)) {
				return true;
			}
		}

		return false;

	}

	public Collection<Attribute> getReadFields() {
		return readFields;
	}

	public Collection<Attribute> getWriteFields() {
		return writeFields;
	}

	public void addInvokeItem(InvokeItem item) {
		if (!this.invokeItems.contains(item)) {
			this.invokeItems.add(item);
			item.setSelf(this);
		}
	}

	public void addReadField(String fieldName) {
		Attribute attribute = this.javaClass.getTheAttribute(fieldName);
		if (attribute != null && !this.readFields.contains(attribute)) {
			this.readFields.add(attribute);
		}
	}

	public void addWriteField(String fieldName) {
		Attribute attribute = this.javaClass.getTheAttribute(fieldName);
		if (attribute != null && !this.writeFields.contains(attribute)) {
			this.writeFields.add(attribute);
		}
	}

	public String getSignature() {
		return signature;
	}

	public Collection<String> getArgumentTypes() {
		if (this.argTypes == null) {
			this.argTypes = new ArrayList<String>();
			try {
				int pos = signature.lastIndexOf(')');
				if (pos > 0) {
					String paramTypes = signature.substring(1, pos);
					Collection<String> types = ParseUtil.signatureToTypes(paramTypes);
					for (String type : types) {
						if (!this.argTypes.contains(type)) {
							this.argTypes.add(type);
						}
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.argTypes;
	}

	public int getArgumentCount() {
		return argumentCount;
	}

	public String getArgumentInfo() {
		return this.info.substring(this.info.indexOf('(') + 1, this.info.indexOf(')'));
	}

	private int calArgumentCount() {
		int pos = this.info.indexOf('(');
		int pos1 = this.info.indexOf(')');
		if (pos + 1 == pos1) {
			return 0;
		} else {
			int count = 1;
			pos1 = this.info.indexOf(',', pos + 1);
			while (pos1 != -1) {
				pos = pos1;
				pos1 = this.info.indexOf(',', pos + 1);
				count++;
			}
			return count;
		}
	}

	public Collection<String> getReturnTypes() {
		if (this.returnTypes == null) {
			this.returnTypes = new ArrayList<String>();
			try {
				String returnType = signature.substring(signature.lastIndexOf(')') + 1);
				Collection<String> types = ParseUtil.signatureToTypes(returnType);
				for (String type : types) {
					if (!this.returnTypes.contains(type)) {
						this.returnTypes.add(type);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.returnTypes;
	}

	public Collection<JavaClass> getArgClassTypes() {
		return argClassTypes;
	}

	public void setArgClassTypes(Collection<JavaClass> argClassTypes) {
		this.argClassTypes = argClassTypes;
	}

	public Collection<JavaClass> getReturnClassTypes() {
		return returnClassTypes;
	}

	public void setReturnClassTypes(Collection<JavaClass> returnClassTypes) {
		this.returnClassTypes = returnClassTypes;
	}

	public boolean existReturn() {
		return this.getReturnTypes().size() > 0;
	}

	public boolean isConstruction() {
		return this.name.equals("<init>");
	}

	public int getSelfLineCount() {
		return selfLineCount;
	}

	public void setSelfLineCount(int selfLineCount) {
		this.selfLineCount = selfLineCount;
	}

	public boolean isIncludeTransactionalAnnotation() {
		return isIncludeTransactionalAnnotation;
	}

	public void setIncludeTransactionalAnnotation(boolean isIncludeTransactionalAnnotation) {
		this.isIncludeTransactionalAnnotation = isIncludeTransactionalAnnotation;
	}

	public RequestMapping getRequestMapping() {
		return requestMapping;
	}

	public void setRequestMapping(RequestMapping requestMapping) {
		this.requestMapping = requestMapping;
	}

	public String getRequestMappingValue() {
		if (this.javaClass.getDetail().getRequestMapping() == null || this.requestMapping == null) {
			return null;
		}
		StringBuilder value = new StringBuilder();
		if (this.javaClass.getDetail().getRequestMapping().getValue().length() > 0) {
			if (this.javaClass.getDetail().getRequestMapping().getValue().startsWith("/")) {
				value.append(this.javaClass.getDetail().getRequestMapping().getValue());
			} else {
				value.append("/");
				value.append(this.javaClass.getDetail().getRequestMapping().getValue());
			}
		}

		if (this.requestMapping.getValue().length() > 0) {
			if (this.requestMapping.getValue().startsWith("/")) {
				value.append(this.requestMapping.getValue());
			} else {
				value.append("/");
				value.append(this.requestMapping.getValue());
			}
		}
		return value.toString();

	}

	public String getRequestMappingValueNoVariable() {
		String requestMappingValue = this.getRequestMappingValue();
		if (requestMappingValue == null) {
			return null;
		} else {
			if (requestMappingValue.indexOf("{") == -1) {
				return requestMappingValue;
			} else {
				StringBuilder valueNoVariable = new StringBuilder();
				valueNoVariable.append("/");
				for (String segment : requestMappingValue.split("/")) {
					if (segment.length() > 0 && !segment.startsWith("{")) {
						valueNoVariable.append(segment);
						valueNoVariable.append("/");
					}
				}
				if (!requestMappingValue.endsWith("/")) {
					valueNoVariable.delete(valueNoVariable.length() - 1, valueNoVariable.length());
				}
				return valueNoVariable.toString();
			}
		}
	}

	public boolean isOverride(Method method) {
		if (this.getJavaClass().equals(method.getJavaClass())) {
			return false;
		}
		if (!this.getJavaClass().getSupers().contains(method.getJavaClass())) {
			return false;
		}
		if (!this.name.equals(method.name)) {
			return false;
		}
		return this.signature.equals(method.signature);
	}

	public String getJavaClassId() {
		return javaClassId;
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();

		this.invokedMethods = new HashSet<Method>();
	}

	@Override
	public String toString() {

		StringBuilder info1 = new StringBuilder();
		info1.append("Method [" + info + " Class=" + this.getJavaClass().getName() + "]");
		info1.append("\n		selfLineCount : " + this.selfLineCount);

		if (this.isIncludeTransactionalAnnotation) {
			info1.append("\n		isIncludeTransactionalAnnotation : true");
		}

		if (this.requestMapping != null) {
			info1.append("\n		requestMapping : " + this.getRequestMappingValue());
			info1.append(" method : " + this.requestMapping.getMethod());
		}

		if (this.readFields.size() != 0) {
			info1.append("\n		readFields:");
			for (Attribute attribute : this.readFields) {
				info1.append("\n				");
				info1.append(attribute);
			}
		}

		if (this.writeFields.size() != 0) {
			info1.append("\n		writeFields:");
			for (Attribute attribute : this.writeFields) {
				info1.append("\n				");
				info1.append(attribute);
			}
		}

		if (this.invokeItems.size() != 0) {
			info1.append("\n		invokeItems:");
			for (InvokeItem item : this.invokeItems) {
				info1.append("\n				");
				info1.append(item);
			}
		}
		return info1.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + ((javaClassId == null) ? 0 : javaClassId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Method other = (Method) obj;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		if (javaClassId == null) {
			if (other.javaClassId != null)
				return false;
		} else if (!javaClassId.equals(other.javaClassId))
			return false;
		return true;
	}
}
