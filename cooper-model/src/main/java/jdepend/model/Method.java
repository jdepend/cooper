package jdepend.model;

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

	private JavaClass javaClass;

	private String name;

	private String signature;

	private String info;

	private Collection<InvokeItem> invokeItems;

	private Collection<String> readFields;

	private Collection<String> writeFields;

	private int selfLineCount;

	private int argumentCount;
	
	private boolean isIncludeTransactionalAnnotation;

	private transient Collection<String> argTypes;

	private transient Collection<String> returnTypes;

	private transient Collection<JavaClass> argClassTypes;

	private transient Collection<JavaClass> returnClassTypes;

	private transient Collection<Method> invokeMethods;

	public Method() {
	}

	public Method(JavaClass javaClass, org.apache.bcel.classfile.Method method) {
		this.javaClass = javaClass;
		this.access_flags = method.getAccessFlags();
		this.name = method.getName();
		this.signature = SignatureUtil.getSignature(method);
		this.info = method.toString();
		this.argumentCount = this.calArgumentCount();
		this.invokeItems = new ArrayList<InvokeItem>();
		this.readFields = new ArrayList<String>();
		this.writeFields = new ArrayList<String>();
		this.isIncludeTransactionalAnnotation = false;
		this.selfLineCount = -1;
	}

	public Method(JavaClass javaClass, Method method) {
		this.javaClass = javaClass;
		this.access_flags = method.access_flags;
		this.name = method.name;
		this.signature = method.signature;
		this.info = method.info;
		this.argumentCount = method.argumentCount;
		this.invokeItems = method.invokeItems;
		this.readFields = method.readFields;
		this.writeFields = method.writeFields;
		this.selfLineCount = method.selfLineCount;
		this.isIncludeTransactionalAnnotation = method.isIncludeTransactionalAnnotation;
	}

	public String getName() {
		return name;
	}

	public JavaClass getJavaClass() {
		return javaClass;
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

	public Collection<Method> getInvokedMethods(Collection<Method> methods) {
		Collection<Method> invokedMethods = new ArrayList<Method>();
		for (Method invokeMethod : methods) {
			if (!this.equals(invokeMethod) && this.isInvoked(invokeMethod)) {
				invokedMethods.add(invokeMethod);
			}
		}
		return invokedMethods;
	}

	private boolean isInvoked(Method method) {
		if (this.isPrivate()) {
			return false;
		}

		if (method.getInvokeMethods().contains(this)) {
			return true;
		}

		Method overrideMethod = this.javaClass.getOverrideMethods().get(this);
		if (overrideMethod != null && method.getInvokeMethods().equals(overrideMethod)) {
			return true;
		}

		return false;

	}

	public Collection<String> getReadFields() {
		return readFields;
	}

	public Collection<String> getWriteFields() {
		return writeFields;
	}

	public void addInvokeItem(InvokeItem item) {
		if (!this.invokeItems.contains(item)) {
			this.invokeItems.add(item);
		}
	}

	public void addReadField(String fieldName) {
		if (!this.readFields.contains(fieldName)) {
			this.readFields.add(fieldName);
		}
	}

	public void addWriteField(String fieldName) {
		if (!this.writeFields.contains(fieldName)) {
			this.writeFields.add(fieldName);
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

	@Override
	public String toString() {
		return "Method [info=" + info + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result + ((javaClass == null) ? 0 : javaClass.hashCode());
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
		if (javaClass == null) {
			if (other.javaClass != null)
				return false;
		} else if (!javaClass.equals(other.javaClass))
			return false;
		return true;
	}
}
