package jdepend.metadata.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public final class ParseUtil {

	private final static Map<String, String> baseTypes = new HashMap<String, String>();

	static {
		baseTypes.put("Z", "boolean");
		baseTypes.put("B", "byte");
		baseTypes.put("C", "char");
		baseTypes.put("D", "double");
		baseTypes.put("F", "float");
		baseTypes.put("I", "int");
		baseTypes.put("J", "long");
		baseTypes.put("S", "short");
		baseTypes.put("V", "void");
	}

	private final static Collection<String> stateClassTypes = new HashSet<String>();

	static {
		stateClassTypes.add("java.lang.String");
		stateClassTypes.add("java.lang.Boolean");
		stateClassTypes.add("java.lang.Byte");
		stateClassTypes.add("java.lang.Double");
		stateClassTypes.add("java.lang.Enum");
		stateClassTypes.add("java.lang.Float");
		stateClassTypes.add("java.lang.Integer");
		stateClassTypes.add("java.lang.Long");
		stateClassTypes.add("java.lang.Number");
		stateClassTypes.add("java.lang.Short");
		stateClassTypes.add("java.lang.StringBuffer");
		stateClassTypes.add("java.lang.StringBuilder");
		stateClassTypes.add("java.lang.Void");
	}

	public static final char CLASS_DESCRIPTOR = 'L';
	public static final char CLASS_DESCRIPTOR1 = 'I';

	public static String slashesToDots(String s) {
		return s.replace('/', '.');
	}

	public static String getType(String s) {
		if (s.startsWith("[")) {
			if (s.startsWith("[[[D")) {
				s = s.substring(4);
			}
			if (s.startsWith("[[D")) {
				s = s.substring(3);
			}
			if (s.startsWith("[D")) {
				s = s.substring(2);
			}
			if (s.startsWith("[[[L")) {
				s = s.substring(4);
			}
			if (s.startsWith("[[L")) {
				s = s.substring(3);
			}
			if (s.startsWith("[L")) {
				s = s.substring(2);
			}
			if (s.startsWith("[[I")) {
				s = s.substring(3);
			}
			if (s.startsWith("[[J")) {
				s = s.substring(3);
			}
			if (s.startsWith("[J")) {
				s = s.substring(2);
			}
			if (s.startsWith("[[F")) {
				s = s.substring(3);
			}
			if (s.startsWith("[F")) {
				s = s.substring(2);
			}
			if (s.startsWith("[I")) {
				s = s.substring(2);
			}
			if (s.startsWith("[B")) {
				s = s.substring(2);
			}
			if (s.startsWith("[Z")) {
				s = s.substring(2);
			}
			if (s.startsWith("[C")) {
				s = s.substring(2);
			}
			if (s.startsWith("[S")) {
				s = s.substring(2);
			}
		}
		if (s.endsWith(";")) {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}

	public static String getPackageName(String className) {
		int index = className.lastIndexOf(".");
		if (index > 0) {
			int startPos = 0;
			if (className.startsWith("[L")) {
				startPos = 2;
			}
			return className.substring(startPos, index);
		} else {
			if (isBaseType(className)) {
				return null;
			} else {
				return "Default";
			}
		}
	}

	public static boolean existCollectionType(String signature) {
		return signature.indexOf('<') != -1;
	}

	public static Collection<String> signatureToTypes(String signature) {
		Collection<String> rtnTypes = new ArrayList<String>();

		if (signature == null || signature.length() == 0) {
			return rtnTypes;
		}

		// 预处理 分离集合类型
		if (signature.indexOf('<') != -1) {
			signature = signature.replaceAll("<", ";");
			signature = signature.replaceAll(">", ";");
		}

		String[] types = signature.split(";");

		for (String type : types) {
			if (type.length() > 0) {
				// 处理简单类型
				String baseType = baseTypes.get(signature.substring(0, 1));
				if (baseType != null) {
					rtnTypes.add(baseType);
				}
				// 处理普通类型
				int startIndex = type.indexOf(CLASS_DESCRIPTOR);
				if (startIndex != -1) {
					rtnTypes.add(slashesToDots(type.substring(startIndex + 1)));
				}
			}
		}
		return rtnTypes;
	}

	public static String filterGenerics(String signature) {
		StringBuilder rtn = new StringBuilder();
		int start = 0;
		int end = signature.indexOf('<');
		int pos;
		while (end != -1) {
			rtn.append(signature.substring(start, end));
			start = signature.indexOf('>', end) + 1;
			pos = signature.indexOf('<', end + 1);
			if (pos != -1 && pos < start) {
				start = signature.indexOf('>', start) + 1;
			}
			end = signature.indexOf('<', start);
		}
		rtn.append(signature.substring(start));
		return rtn.toString();
	}

	public static String getMethodInfo(String info) {
		int pos = info.indexOf("[(Unknown");
		if (pos == -1) {
			return info;
		} else {
			return info.substring(0, pos);
		}
	}

	/**
	 * 是否为基本类型
	 * 
	 * @param types
	 * @return
	 */
	public static boolean isBaseType(String type) {
		if (baseTypes.values().contains(type)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否为有状态的对象类型
	 * 
	 * @param types
	 * @return
	 */
	public static boolean isStateClassType(String type) {
		if (stateClassTypes.contains(type)) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		// String c = "(()Ljava/util/List<Ljdepend/core/JDependUnit;>;)";
		// String c = "()Ljava/util/List<[B>;";
		// String c =
		// "(<T:Lcom/neusoft/comm/utils/Entity;>(Ljava/lang/String;)TT;)";

		String c = "(Ljava/lang/String;)Lcom/neusoft/edu/entity/EntityContainer<Lcom/neusoft/edu/morg/entities/UnitEntity;>;";
		Collection<String> types = ParseUtil.signatureToTypes(c);
		for (String type : types) {
			System.out.println(type);
		}
		// String c =
		// "Ljdepend/model/JavaClass;Ljdepend/parse/JavaClassAnalysis";

		// String c =
		// "()Ljava/util/Map<Ljava/lang/String;Ljava/util/ArrayList<Ljava/lang/String;>;>;";
		// System.out.println(ParseUtil.filterGenerics(c));

	}

}
