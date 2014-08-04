package jdepend.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class JavaClassDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3437990183453118207L;

	private final JavaClass javaClass;

	private String superClassName;

	private transient JavaClass superClass;

	private Collection<String> interfaceNames = new ArrayList<String>();

	private transient Collection<JavaClass> interfaces = new ArrayList<JavaClass>();

	private transient Collection<String> attributeTypes;

	private transient Collection<String> paramTypes;

	private final Collection<String> variableTypes = new ArrayList<String>();

	private final Collection<Attribute> attributes = new ArrayList<Attribute>();

	private final Collection<Method> methods = new ArrayList<Method>();

	private final Collection<TableInfo> tables = new ArrayList<TableInfo>();

	public JavaClassDetail(JavaClass javaClass) {
		this.javaClass = javaClass;
	}

	public Collection<String> getAttributeTypes() {
		if (this.attributeTypes == null) {
			this.attributeTypes = new HashSet<String>();
			for (Attribute attribute : this.attributes) {
				for (String type : attribute.getTypes()) {
					if (!this.attributeTypes.contains(type)) {
						this.attributeTypes.add(type);
					}
				}
			}
		}
		return attributeTypes;
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
					if (!this.paramTypes.contains(type)) {
						this.paramTypes.add(type);
					}
				}
				for (String type : method.getReturnTypes()) {
					if (!this.paramTypes.contains(type)) {
						this.paramTypes.add(type);
					}
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
		}

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
	}

	public void setInterfaces(Collection<JavaClass> interfaces) {
		this.interfaces = interfaces;
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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final JavaClassDetail other = (JavaClassDetail) obj;
		if (javaClass == null) {
			if (other.javaClass != null)
				return false;
		} else if (!javaClass.equals(other.javaClass))
			return false;
		return true;
	}

}
