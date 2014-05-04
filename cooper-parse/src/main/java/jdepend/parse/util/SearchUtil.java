package jdepend.parse.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.model.JavaClass;
import jdepend.model.JavaPackage;
import jdepend.parse.Parse;
import jdepend.parse.ParseListener;

public final class SearchUtil {

	private Parse jdepend = new Parse();

	private Collection<JavaPackage> javaPackages;

	private Collection<JavaClass> javaClasses;

	private Integer classCount;

	public SearchUtil() {

	}

	public void addFilters(List<String> filters) {
		this.jdepend.addFilteredPackages(filters);
	}

	public SearchUtil(List<String> paths) {

		for (String path : paths) {
			try {
				this.jdepend.addDirectorys(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public int getClassCount() {
		if (classCount == null) {
			classCount = this.jdepend.countClasses();
		}
		return classCount;
	}

	public Collection<JavaClass> getClasses() {
		if (javaClasses == null) {
			try {
				this.analyse();
			} catch (JDependException e) {
				e.printStackTrace();
			}
		}
		return javaClasses;
	}

	public Collection<JavaPackage> getPackages() {
		if (javaPackages == null) {
			try {
				this.analyse();
			} catch (JDependException e) {
				e.printStackTrace();
			}
		}
		return javaPackages;
	}

	private void analyse() throws JDependException {

		this.javaPackages = new ArrayList<JavaPackage>(this.jdepend.execute());

		this.javaClasses = new ArrayList<JavaClass>();
		for (JavaPackage javaPackage : javaPackages) {
			javaClasses.addAll(javaPackage.getClasses());
		}
	}

	public void addPath(String path) {
		try {
			this.jdepend.addDirectorys(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setBuildClassRelation(boolean isBuildClassRelation) {
		this.jdepend.setBuildClassRelation(isBuildClassRelation);
	}

	public void addParseListener(ParseListener listener) {
		this.jdepend.addParseListener(listener);
	}

}
