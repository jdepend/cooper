package jdepend.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import jdepend.model.util.JavaClassCollection;

public class JavaClassDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3437990183453118207L;

	private final JavaClass javaClass;

	private String superClassName;

	private Collection<String> interfaceNames = new ArrayList<String>();

	private final Collection<Attribute> attributes = new ArrayList<Attribute>();

	private final Collection<Method> methods = new ArrayList<Method>();

	private final Collection<String> variableTypes = new ArrayList<String>();

	private final Collection<TableInfo> tables = new ArrayList<TableInfo>();

	private RequestMapping requestMapping;

	private boolean httpCaller;

	private transient JavaClass superClass;

	private transient Collection<JavaClass> interfaces = new ArrayList<JavaClass>();

	private transient Collection<String> attributeTypes;

	private transient Collection<JavaClass> attributeClasses;

	private transient Collection<String> paramTypes;

	private transient Map<String, Attribute> attributeForNames = new HashMap<String, Attribute>();

	public JavaClassDetail(JavaClass javaClass) {
		this.javaClass = javaClass;
	}

	public Collection<String> getAttributeTypes() {
		if (this.attributeTypes == null) {
			this.attributeTypes = new HashSet<String>();
			for (Attribute attribute : this.attributes) {
				for (String type : attribute.getTypes()) {
					this.attributeTypes.add(type);
				}
			}
		}
		return attributeTypes;
	}

	public Collection<JavaClass> getAttributeClasses() {
		if (this.attributeClasses == null) {
			this.attributeClasses = new HashSet<JavaClass>();
			for (Attribute attribute : this.attributes) {
				for (JavaClass type : attribute.getTypeClasses()) {
					this.attributeClasses.add(type);
				}
			}
		}
		return attributeClasses;
	}

	public Collection<String> getInterfaceNames() {
		return interfaceNames;
	}

	public void addInterfaceName(String interfaceName) {
		if (!this.interfaceNames.contains(interfaceName) && !this.javaClass.getName().equals(interfaceName)) {
			this.interfaceNames.add(interfaceName);
		}
	}

	public Collection<String> getParamTypes() {
		if (this.paramTypes == null) {
			this.paramTypes = new HashSet<String>();
			for (Method method : this.methods) {
				for (String type : method.getArgumentTypes()) {
					this.paramTypes.add(type);
				}
				for (String type : method.getReturnTypes()) {
					this.paramTypes.add(type);
				}
			}
		}
		return paramTypes;
	}

	public String getSuperClassName() {
		return superClassName;
	}

	public void setSuperClassName(String superClassName) {
		this.superClassName = superClassName;
	}

	public Collection<String> getVariableTypes() {
		return variableTypes;
	}

	public void addVariableType(String variableType) {
		if (!this.javaClass.getName().equals(variableType) && !this.variableTypes.contains(variableType)
				&& !this.getAttributeTypes().contains(variableType) && !this.getParamTypes().contains(variableType)) {
			this.variableTypes.add(variableType);
		}
	}

	public Collection<TableInfo> getTables() {
		return tables;
	}

	public void addTable(TableInfo table) {
		if (!this.tables.contains(table)) {
			this.tables.add(table);
		}

	}

	public Collection<Attribute> getAttributes() {
		return attributes;
	}

	public void addAttribute(Attribute attribute) {
		if (!this.attributes.contains(attribute)) {
			this.attributes.add(attribute);
			this.attributeForNames.put(attribute.getName(), attribute);
		}
	}

	public Attribute getTheAttribute(String name) {
		return this.attributeForNames.get(name);
	}

	public Collection<Method> getMethods() {
		return methods;
	}

	public void addMethod(Method method) {
		if (!this.methods.contains(method)) {
			this.methods.add(method);
		}
	}

	public Collection<String> getSupers() {
		Collection<String> supers = new ArrayList<String>();
		for (String interfaceName : this.getInterfaceNames()) {
			supers.add(interfaceName);
		}
		if (this.superClassName != null) {
			supers.add(this.superClassName);
		}
		return supers;
	}

	public void setSuperClass(JavaClass superClass) {
		this.superClass = superClass;
		this.superClass.addSubClass(this.javaClass);
	}

	public void setInterfaces(Collection<JavaClass> interfaces) {
		this.interfaces = interfaces;

		for (JavaClass interfaceClass : this.interfaces) {
			interfaceClass.addSubClass(this.javaClass);
		}
	}

	public void setInterfaceNames(Collection<String> interfaceNames) {
		this.interfaceNames = interfaceNames;
	}

	public JavaClass getSuperClass() {
		return superClass;
	}

	public Collection<JavaClass> getInterfaces() {
		return interfaces;
	}

	public RequestMapping getRequestMapping() {
		return requestMapping;
	}

	public String getRequestMappingValue() {
		if (this.requestMapping == null) {
			return null;
		}

		if (this.requestMapping.getValue().startsWith("/")) {
			return this.requestMapping.getValue();
		} else {
			return "/" + this.requestMapping.getValue();
		}
	}

	public void setRequestMapping(RequestMapping requestMapping) {
		this.requestMapping = requestMapping;
	}

	public boolean isHttpCaller() {
		return httpCaller;
	}

	public void setHttpCaller(boolean httpCaller) {
		this.httpCaller = httpCaller;
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		this.interfaces = new HashSet<JavaClass>();
		this.attributeForNames = new HashMap<String, Attribute>();
	}

	public JavaClassDetail clone(JavaClass javaClass) {

		JavaClassDetail obj = new JavaClassDetail(javaClass);

		obj.setSuperClassName(this.getSuperClassName());
		obj.setInterfaceNames(this.getInterfaceNames());

		for (Attribute attribute : this.getAttributes()) {
			obj.addAttribute(new Attribute(attribute.getJavaClass().getId(), attribute));
		}

		for (Method method : this.getMethods()) {
			obj.addMethod(new Method(method.getJavaClass().getId(), method));
		}

		for (String name : this.getVariableTypes()) {
			obj.addVariableType(name);
		}

		for (TableInfo tableRelationInfo : this.getTables()) {
			obj.addTable(new TableInfo(tableRelationInfo));
		}

		obj.requestMapping = this.requestMapping;
		obj.httpCaller = this.httpCaller;

		return obj;
	}

	public void supply(JavaClassCollection javaClasses) {

		// 填充superClass和interfaces
		JavaClass superClass = javaClasses.getTheClass(javaClass.getPlace(), this.getSuperClassName());
		if (superClass != null) {
			this.setSuperClass(superClass);
		}
		Collection<JavaClass> interfaces = new HashSet<JavaClass>();
		for (String interfaceName : this.getInterfaceNames()) {
			JavaClass interfaceClass = javaClasses.getTheClass(javaClass.getPlace(), interfaceName);
			if (interfaceClass != null) {
				interfaces.add(interfaceClass);
			}
		}
		this.setInterfaces(interfaces);

		// 填充Attribute中的JavaClass
		for (Attribute attribute : this.getAttributes()) {
			attribute.supply(javaClasses);
		}

		// 填充Method中的JavaClass
		for (Method method : this.getMethods()) {
			method.supply(javaClasses);
		}
	}

	@Override
	public String toString() {
		StringBuilder content = new StringBuilder(500);

		content.append("ImportPackages:\n");
		for (String importPackage : this.javaClass.getImportedPackages()) {
			content.append(tab());
			content.append(importPackage);
			content.append("\n");
		}
		content.append("\n");

		if (superClassName != null) {
			content.append("SuperClassName:");
			content.append(superClassName);
			content.append("\n");
		}

		if (interfaceNames != null && interfaceNames.size() > 0) {
			content.append("InterfaceNames:\n");
			for (String interfaceName : interfaceNames) {
				content.append(tab());
				content.append(interfaceName);
				content.append("\n");
			}
		}
		content.append("isAbstract:");
		content.append(this.javaClass.isAbstract());
		content.append("\n");
		if (this.javaClass.getPlace() != null) {
			content.append("Place:");
			content.append(this.javaClass.getPlace());
			content.append("\n");
		}
		content.append("Name:");
		content.append(this.javaClass.getName());
		content.append("\n");

		if (this.getRequestMappingValue() != null) {
			content.append("RequestMapping:");
			content.append(this.getRequestMappingValue());
			content.append("\n");
		}

		if (this.javaClass.isIncludeTransactionalAnnotation()) {
			content.append("isIncludeTransactionalAnnotation:true");
			content.append("\n");
		}

		content.append("\n");

		if (this.getAttributeTypes() != null && this.getAttributeTypes().size() > 0) {
			content.append("attributeTypes:\n");
			for (String attributeType : this.getAttributeTypes()) {
				content.append(tab());
				content.append(attributeType);
				content.append("\n");
			}
			content.append("\n");
		}
		if (this.getParamTypes() != null && this.getParamTypes().size() > 0) {
			content.append("paramTypes:\n");
			for (String paramType : this.getParamTypes()) {
				content.append(tab());
				content.append(paramType);
				content.append("\n");
			}
			content.append("\n");
		}
		if (variableTypes != null && variableTypes.size() > 0) {
			content.append("variableTypes:\n");
			for (String variableType : variableTypes) {
				content.append(tab());
				content.append(variableType);
				content.append("\n");
			}
			content.append("\n");
		}
		if (attributes != null && attributes.size() > 0) {
			content.append("attributes:\n");
			for (Attribute attribute : attributes) {
				content.append(tab());
				content.append(attribute);
				content.append("\n");
			}
			content.append("\n");
		}
		if (methods != null && methods.size() > 0) {
			content.append("methods:\n");
			for (Method method : methods) {
				content.append(tab());
				content.append(method);
				content.append("\n");
			}
			content.append("\n");
		}

		if (tables != null && tables.size() > 0) {
			content.append("tables:\n");
			for (TableInfo table : tables) {
				content.append(tab());
				content.append(table);
				content.append("\n");
			}
			content.append("\n");
		}

		if (this.javaClass.getInnerClasses().size() > 0) {
			content.append("innerClasses:\n");
			for (JavaClass innerClass : this.javaClass.getInnerClasses()) {
				content.append(tab());
				content.append(innerClass.getName());
				content.append("\n");
			}
			content.append("\n");
		}

		return content.toString();
	}

	private String tab() {
		return "	";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.getClass().getName().hashCode();
		result = prime * result + ((javaClass == null) ? 0 : javaClass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		final JavaClassDetail other = (JavaClassDetail) obj;
		if (!javaClass.equals(other.javaClass))
			return false;
		return true;
	}

}
