package jdepend.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import jdepend.metadata.util.JavaClassCollection;
import jdepend.metadata.util.ParseUtil;
import jdepend.metadata.util.SignatureUtil;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Field;

public class Attribute implements Serializable {

	private static final long serialVersionUID = 5330876838708287382L;

	private String javaClassId;

	private int access_flags;

	private String info;

	private String name;

	private String staticValue;

	private String signature;

	private List<String> types;

	private transient JavaClass javaClass;

	private transient Collection<JavaClass> typeClasses;

	public Attribute(JavaClass javaClass, Field field) {
		this.javaClass = javaClass;
		this.javaClassId = javaClass.getId();

		this.access_flags = field.getAccessFlags();
		this.info = field.toString();
		this.name = field.getName();
		this.signature = SignatureUtil.getSignature(field);
		this.types = new ArrayList<String>();
		for (String type : ParseUtil.signatureToTypes(this.signature)) {
			this.types.add(type);
		}

		if (field.isStatic() && field.getConstantValue() != null) {
			staticValue = field.getConstantValue().toString();
		}
	}

	public Attribute(String javaClassId, Attribute attribute) {
		this.javaClassId = javaClassId;

		this.access_flags = attribute.access_flags;
		this.info = attribute.info;
		this.name = attribute.name;
		this.signature = attribute.signature;
		this.types = new ArrayList<String>();
		for (String type : attribute.types) {
			this.types.add(type);
		}
		this.staticValue = attribute.staticValue;
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

	public String getSignature() {
		return signature;
	}

	public String getName() {
		return name;
	}

	public List<String> getTypes() {
		return types;
	}

	public Collection<JavaClass> getTypeClasses() {
		return this.typeClasses;
	}

	public void setTypeClasses(Collection<JavaClass> typeClasses) {
		this.typeClasses = typeClasses;
	}

	public boolean existCollectionType() {
		return ParseUtil.existCollectionType(signature);
	}

	public String getStaticValue() {
		return staticValue;
	}

	public void setStaticValue(String staticValue) {
		this.staticValue = staticValue;
	}

	public final boolean isPublic() {
		return (access_flags & Constants.ACC_PUBLIC) != 0;
	}

	public final boolean isPrivate() {
		return (access_flags & Constants.ACC_PRIVATE) != 0;
	}

	public final boolean isProtected() {
		return (access_flags & Constants.ACC_PROTECTED) != 0;
	}

	public final boolean isStatic() {
		return (access_flags & Constants.ACC_STATIC) != 0;
	}

	public final boolean isFinal() {
		return (access_flags & Constants.ACC_FINAL) != 0;
	}

	public final boolean isInterface() {
		for (JavaClass javaClass : this.typeClasses) {
			if (javaClass.isInterface()) {
				return true;
			}
		}
		return false;
	}

	public final boolean isAbstract() {
		for (JavaClass javaClass : this.typeClasses) {
			if (javaClass.isAbstract()) {
				return true;
			}
		}
		return false;
	}

	public final boolean isAnnotation() {
		return (access_flags & Constants.ACC_ANNOTATION) != 0;
	}

	public final boolean isEnum() {
		for (JavaClass javaClass : this.typeClasses) {
			if (javaClass.isEnum()) {
				return true;
			}
		}
		return false;
	}

	public void supply(JavaClassCollection javaClasses) {

		Collection<JavaClass> attributeTypes;
		JavaClass attributeTypeClass;

		// 填充JavaClass
		if (this.javaClass == null) {
			this.javaClass = javaClasses.getTheClass(this.javaClassId);
		}

		attributeTypes = new HashSet<JavaClass>();
		for (String type : this.getTypes()) {
			attributeTypeClass = javaClasses.getTheClass(javaClass.getPlace(), type);
			if (attributeTypeClass != null) {
				attributeTypes.add(attributeTypeClass);
			}
		}
		this.setTypeClasses(attributeTypes);
	}

	public void filterExternalJavaClass(Collection<JavaClass> javaClasses) {
		for (JavaClass typeClass : this.typeClasses) {
			if (!javaClasses.contains(typeClass)) {
				this.typeClasses.remove(typeClass);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder rtn = new StringBuilder();
		rtn.append("Attribute [info=");
		rtn.append(info);
		if (this.isStatic() && this.staticValue != null && info.indexOf('=') == -1) {
			rtn.append(" = ");
			rtn.append(staticValue);
		}
		rtn.append("]");

		return rtn.toString();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.javaClassId == null) ? 0 : this.javaClassId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Attribute other = (Attribute) obj;
		if (!javaClassId.equals(other.javaClassId))
			return false;
		if (!name.equals(other.name))
			return false;
		return true;
	}
}
