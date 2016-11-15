package jdepend.metadata;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import jdepend.framework.log.LogUtil;
import jdepend.metadata.annotation.AnnotationDefs;
import jdepend.metadata.annotation.AnnotationParse;
import jdepend.metadata.annotation.RequestMapping;
import jdepend.metadata.annotation.Transactional;
import jdepend.metadata.util.JavaClassCollection;
import jdepend.metadata.util.MethodUtil;
import jdepend.metadata.util.ParseUtil;
import jdepend.metadata.util.SignatureUtil;

import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Utility;

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

	private AnnotationDefs annotationDefs;

	private transient Collection<String> argTypes;

	private transient Collection<String> returnTypes;

	private transient Collection<JavaClass> argClassTypes;

	private transient Collection<JavaClass> returnClassTypes;

	private transient Collection<Method> invokeMethods;

	private transient Collection<InvokeItem> invokedItems;

	private transient Collection<Method> invokedMethods;

	private transient Collection<InvokeItem> cascadeInvokedItems;

	private transient Collection<Method> cascadeInvokedMethods;

	private transient JavaClass javaClass;
	
	public final static String CLINIT = "<clinit>";

	public Method() {
	}

	public Method(JavaClass javaClass, org.apache.bcel.classfile.Method method, boolean isParseAnnotation) {
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
		this.selfLineCount = -1;
		this.invokedItems = new HashSet<InvokeItem>();

		this.annotationDefs = new AnnotationDefs();
		// 处理Annotation
		if (isParseAnnotation) {
			for (AnnotationEntry annotationEntry : method.getAnnotationEntries()) {
				if (annotationEntry.getAnnotationType().equals(AnnotationParse.Transactional)) {
					this.annotationDefs.setTransactional(AnnotationParse.parseTransactional(annotationEntry));
				} else if (annotationEntry.getAnnotationType().equals(AnnotationParse.RequestMapping)) {
					this.annotationDefs.setRequestMapping(AnnotationParse.parseRequestMapping(annotationEntry));
				}
			}
		}
	}

	public Method(String javaClassId, Method method) {

		this.javaClassId = javaClassId;
		this.access_flags = method.access_flags;
		this.name = method.name;
		this.signature = method.signature;
		this.info = method.info;
		this.argumentCount = method.argumentCount;

		this.invokedItems = new HashSet<InvokeItem>();

		this.invokeItems = method.invokeItems;
		for (InvokeItem invokeItem : this.invokeItems) {
			invokeItem.setCaller(this);
		}

		this.readFields = method.readFields;
		this.writeFields = method.writeFields;
		this.selfLineCount = method.selfLineCount;
		this.annotationDefs = method.annotationDefs;
	}

	public String getAccessFlagName() {
		return Utility.accessToString(this.getAccessFlags());
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return this.javaClass.getName() + "." + this.name;
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

	public Transactional getTransactional() {
		return this.annotationDefs.getTransactional();
	}

	public Collection<InvokeItem> getInvokeItems() {
		return invokeItems;
	}

	public synchronized Collection<Method> getInvokeMethods() {
		if (this.invokeMethods == null) {
			this.invokeMethods = new HashSet<Method>();
			for (InvokeItem item : this.invokeItems) {
				this.invokeMethods.add(item.getCallee());
			}
		}
		return this.invokeMethods;
	}

	public Collection<InvokeItem> getInvokedItems() {
		return invokedItems;
	}

	public synchronized void addInvokedItem(InvokeItem invokeItem) {
		invokedItems.add(invokeItem);
	}

	public synchronized Collection<Method> getInvokedMethods() {
		if (this.invokedMethods == null) {
			this.invokedMethods = new HashSet<Method>();
			for (InvokeItem item : this.invokedItems) {
				this.invokedMethods.add(item.getCaller());
			}
		}
		return invokedMethods;
	}

	public synchronized Collection<InvokeItem> getCascadeInvokedItems() {
		if (cascadeInvokedItems == null) {
			cascadeInvokedItems = new HashSet<InvokeItem>();
			cascadeInvokedItems.addAll(this.getInvokedItems());
			for (Method method : this.getInvokedMethods()) {
				cascadeInvokedItems.addAll(method.getInvokedItems());
				cascadeInvokedItems.addAll(method.getCascadeInvokedItems());
			}
		}
		return cascadeInvokedItems;
	}

	public synchronized Collection<Method> getCascadeInvokedMethods() {
		if (cascadeInvokedMethods == null) {
			cascadeInvokedMethods = new HashSet<Method>();
			for (InvokeItem invokeItem : getCascadeInvokedItems()) {
				cascadeInvokedMethods.add(invokeItem.getCaller());
			}
		}
		return cascadeInvokedMethods;
	}

	public Collection<Attribute> getReadFields() {
		return readFields;
	}

	public Collection<Attribute> getWriteFields() {
		return writeFields;
	}

	public synchronized void addInvokeItem(InvokeItem item) {
		if (!this.invokeItems.contains(item)) {
			this.invokeItems.add(item);
			item.setCaller(this);
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

	public synchronized Collection<String> getArgumentTypes() {
		if (this.argTypes == null) {
			this.argTypes = new HashSet<String>();
			try {
				int pos = signature.lastIndexOf(')');
				if (pos > 0) {
					String paramTypes = signature.substring(1, pos);
					Collection<String> types = ParseUtil.signatureToTypes(paramTypes);
					for (String type : types) {
						this.argTypes.add(type);
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

	public synchronized Collection<String> getReturnTypes() {
		if (this.returnTypes == null) {
			this.returnTypes = new HashSet<String>();
			try {
				String returnType = signature.substring(signature.lastIndexOf(')') + 1);
				Collection<String> types = ParseUtil.signatureToTypes(returnType);
				for (String type : types) {
					this.returnTypes.add(type);
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

	public RequestMapping getRequestMapping() {
		return this.annotationDefs.getRequestMapping();
	}

	public String getRequestMappingValue() {
		if (this.javaClass.getDetail().getRequestMapping() == null || this.getRequestMapping() == null) {
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

		if (this.getRequestMapping().getValue().length() > 0) {
			if (this.getRequestMapping().getValue().startsWith("/")) {
				value.append(this.getRequestMapping().getValue());
			} else {
				value.append("/");
				value.append(this.getRequestMapping().getValue());
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

		if (!this.name.equals(method.name)) {
			return false;
		}
		return this.signature.equals(method.signature);
	}

	public String getJavaClassId() {
		return javaClassId;
	}

	public boolean isRemoteInvokeItem() {
		for (InvokeItem item : this.getInvokeItems()) {
			if (item instanceof RemoteInvokeItem) {
				return true;
			}
		}
		return false;
	}

	public boolean isRemoteInvokedItem() {
		for (InvokeItem item : this.getInvokedItems()) {
			if (item instanceof RemoteInvokeItem) {
				return true;
			}
		}
		return false;
	}

	public float getStability() {

		int totalCoupling = this.getInvokedMethods().size() + this.getInvokeMethods().size();
		if (totalCoupling > 0) {
			return (float) this.getInvokeMethods().size() / (float) totalCoupling;
		} else {
			return 0.5F;
		}
	}

	public void supply(JavaClassCollection javaClasses) {

		Collection<JavaClass> argumentTypes;
		JavaClass argumentTypeClass;

		Collection<JavaClass> returnTypes;
		JavaClass returnTypeClass;

		// 填充JavaClass
		if (this.javaClass == null) {
			this.javaClass = javaClasses.getTheClass(this.javaClassId);
		}

		// 填充参数
		argumentTypes = new HashSet<JavaClass>();
		for (String type : this.getArgumentTypes()) {
			argumentTypeClass = javaClasses.getTheClass(javaClass.getPlace(), type);
			if (argumentTypeClass != null) {
				argumentTypes.add(argumentTypeClass);
			}
		}
		this.setArgClassTypes(argumentTypes);
		// 填充返回值
		returnTypes = new HashSet<JavaClass>();
		for (String type : this.getReturnTypes()) {
			returnTypeClass = javaClasses.getTheClass(javaClass.getPlace(), type);
			if (returnTypeClass != null) {
				returnTypes.add(returnTypeClass);
			}
		}
		this.setReturnClassTypes(returnTypes);
	}

	public void supplyInvokeItem(JavaClassCollection javaClasses) {

		Iterator<InvokeItem> it;
		InvokeItem invokeItem;

		Collection<InvokeItem> newInvokeItems = new HashSet<InvokeItem>();
		// 填充Method中的InvokeItem
		it = this.getInvokeItems().iterator();
		while (it.hasNext()) {
			invokeItem = it.next();
			invokeItem.setCaller(this);

			if (!invokeItem.supplyCallee(javaClasses)) {
				LogUtil.getInstance(Method.class).systemWarning(
						"Method [" + this.getJavaClass().getName() + "." + this.getInfo() + "] invokeItem ["
								+ invokeItem + "] not found, is removed.");
				it.remove();
			} else {
				InvokeItem newInvokeItem = invokeItem.transform();
				if (newInvokeItem != null) {
					// 删除原invokeItem的相关引用
					it.remove();
					invokeItem.getCallee().getInvokedItems().remove(invokeItem);
					// 增加新的invokeItem
					newInvokeItems.add(newInvokeItem);
				}
			}
		}
		// 增加新的invokeItems
		for (InvokeItem newInvokeItem : newInvokeItems) {
			this.getInvokeItems().add(newInvokeItem);
			newInvokeItem.getCallee().getInvokedItems().add(newInvokeItem);
		}
	}

	public void filterExternalJavaClass(Collection<JavaClass> javaClasses) {
		for (JavaClass typeClass : this.argClassTypes) {
			if (!javaClasses.contains(typeClass)) {
				this.argClassTypes.remove(typeClass);
			}
		}

		for (JavaClass typeClass : this.returnClassTypes) {
			if (!javaClasses.contains(typeClass)) {
				this.returnClassTypes.remove(typeClass);
			}
		}

		InvokeItem invokeItem;
		Iterator<InvokeItem> it = this.getInvokeItems().iterator();
		while (it.hasNext()) {
			invokeItem = it.next();
			if (!javaClasses.contains(invokeItem.getCallee().getJavaClass())) {
				it.remove();
			}
		}

	}

	public String getMethodInfo() {
		StringBuilder info = new StringBuilder();

		info.append(this.javaClass.getName());
		info.append(".");
		info.append(this.name);
		String argumentInfo = this.getArgumentInfo();
		if (argumentInfo != null && argumentInfo.length() > 0) {
			info.append("(");
			info.append(this.getArgumentInfo());
			info.append(")");
		}

		return info.toString();
	}

	public boolean isBusinessMethod() {
		return MethodUtil.isBusinessMethod(this);
	}

	@Override
	public String toString() {

		StringBuilder info1 = new StringBuilder();
		info1.append("Method [" + info + " Class=" + this.getJavaClass().getName() + "]");
		info1.append("\n		Signature : " + this.signature + "]");
		info1.append("\n		selfLineCount : " + this.selfLineCount);
		info1.append("\n		argumentCount : " + this.getArgumentCount());

		if (this.getArgumentTypes().size() > 0) {
			info1.append("\n		argumentTypes:");
			for (String type : this.getArgumentTypes()) {
				info1.append("\n			" + type);
			}
		}

		if (this.getReturnTypes().size() > 0) {
			info1.append("\n		returnTypes:");
			for (String type : this.getReturnTypes()) {
				info1.append("\n			" + type);
			}
		}

		if (this.annotationDefs.getTransactional() != null) {
			info1.append("\n		Transactional:");
			info1.append(this.annotationDefs.getTransactional());
			info1.append("\n");
		}

		if (this.getRequestMapping() != null) {
			info1.append("\n		requestMapping : " + this.getRequestMappingValue());
			info1.append(" method : " + this.getRequestMapping().getMethod());
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

		if (this.getInvokedItems().size() != 0) {
			info1.append("\n		invokedItems:");
			for (InvokeItem item : this.getInvokedItems()) {
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
		if (getClass() != obj.getClass())
			return false;
		Method other = (Method) obj;
		if (!javaClassId.equals(other.javaClassId))
			return false;
		if (!info.equals(other.info))
			return false;

		return true;
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();

		this.invokedItems = new HashSet<InvokeItem>();
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

}
