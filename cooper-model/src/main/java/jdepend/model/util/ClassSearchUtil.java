package jdepend.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdepend.framework.log.LogUtil;
import jdepend.model.JavaClassUnit;

public class ClassSearchUtil {

	private Map<String, JavaClassUnit> javaClasses;

	private Map<String, ArrayList<String>> classNames = new HashMap<String, ArrayList<String>>();

	private static ClassSearchUtil util = new ClassSearchUtil();

	private ClassSearchUtil() {
	}

	public static ClassSearchUtil getInstance() {
		return util;
	}

	public List<String> getSubClassNames(String superName) {
		synchronized (classNames) {
			if (classNames.get(superName) == null) {
				initSubClassNames(superName);
			}
			return classNames.get(superName);
		}
	}

	public void initSubClassNames(String superName) {
		synchronized (classNames) {
			ArrayList<String> currentClassNames = new ArrayList<String>();
			if (this.javaClasses != null) {
				JavaClassUnit superClass = this.javaClasses.get(superName);
				for (JavaClassUnit subClass : superClass.getSubClasses()) {
					currentClassNames.add(subClass.getName());
				}

				Collections.sort(currentClassNames);
				classNames.put(superName, currentClassNames);
				LogUtil.getInstance(ClassSearchUtil.class).systemLog(
						"init finish " + currentClassNames.size() + " " + superName + ".");
			} else {
				classNames.put(superName, currentClassNames);
			}
		}
	}

	public static List<String> getSelfPath() {
		List<String> self = new ArrayList<String>();
		String[] envpath = System.getProperty("java.class.path").split(";");
		for (String path : envpath) {
			if (path.endsWith("classes") || path.indexOf("cooper.jar") != -1
					|| (path.indexOf("cooper-") != -1 && path.endsWith(".jar"))) {
				self.add(path);
			}
		}
		return self;

	}

	public void setClassList(Collection<JavaClassUnit> classes) {
		this.javaClasses = new HashMap<String, JavaClassUnit>();
		for (JavaClassUnit javaClass : classes) {
			this.javaClasses.put(javaClass.getName(), javaClass);
		}

	}
}
