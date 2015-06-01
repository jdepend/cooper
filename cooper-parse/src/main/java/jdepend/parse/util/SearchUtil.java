package jdepend.parse.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jdepend.framework.exception.JDependException;
import jdepend.framework.file.AnalyzeData;
import jdepend.metadata.JavaClass;
import jdepend.metadata.JavaPackage;
import jdepend.parse.Parse;
import jdepend.parse.ParseListener;

public final class SearchUtil {

	private Parse parse = new Parse();

	private Collection<JavaPackage> javaPackages;

	private Collection<JavaClass> javaClasses;

	private Integer classCount;

	public SearchUtil() {

	}

	public SearchUtil(List<String> paths) {

		for (String path : paths) {
			try {
				this.parse.addDirectorys(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public SearchUtil(AnalyzeData data) {
		this.parse.setAnalyseData(data);
	}

	public void addFilters(List<String> filters) {
		this.parse.addFilteredPackages(filters);
	}

	public int getClassCount() {
		if (classCount == null) {
			classCount = this.parse.countClasses();
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

		this.javaPackages = new ArrayList<JavaPackage>(this.parse.execute());

		this.javaClasses = new ArrayList<JavaClass>();
		for (JavaPackage javaPackage : javaPackages) {
			javaClasses.addAll(javaPackage.getClasses());
		}
	}

	public void addPath(String path) {
		try {
			this.parse.addDirectorys(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addAnalyseData(AnalyzeData data) {
		this.parse.setAnalyseData(data);
	}

	public void setBuildClassRelation(boolean isBuildClassRelation) {
		this.parse.setBuildClassRelation(isBuildClassRelation);
	}

	public void setSupplyJavaClassDetail(boolean isSupplyJavaClassDetail) {
		this.parse.setSupplyJavaClassDetail(isSupplyJavaClassDetail);
	}

	public void setParseConfigs(boolean isParseConfigs) {
		this.parse.setParseConfigs(isParseConfigs);
	}

	public void addParseListener(ParseListener listener) {
		this.parse.addParseListener(listener);
	}

}
