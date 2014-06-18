package jdepend.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ParseUtil {

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

	public static List<String> getPackageNames(String s) {
		List<String> packages = new ArrayList<String>();
		if ((s.length() > 0) && (s.charAt(0) == '[')) {
			for (String type : signatureToTypes(s)) {
				int index = type.lastIndexOf(".");
				if (index > 0) {
					packages.add(type.substring(0, index));
				}
			}
		} else {
			int index = s.lastIndexOf(".");
			if (index > 0) {
				packages.add(s.substring(0, index));
			}
		}

		if (packages.size() == 0) {
			packages.add("Default");
		}
		return packages;
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
			return "Default";
		}
	}

	public static boolean existCollectionType(String signature) {
		return signature.indexOf('<') != -1;
	}

	public static Collection<String> signatureToTypes(String signature) {
		// 预处理 分离集合类型
		if (signature.indexOf('<') != -1) {
			signature = signature.replaceAll("<", ";");
			signature = signature.replaceAll(">", ";");
		}
		String[] types = signature.split(";");

		Collection<String> rtnTypes = new ArrayList<String>();
		for (String type : types) {
			if (type.length() > 0) {
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
