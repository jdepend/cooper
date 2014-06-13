package jdepend.parse.impl;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jdepend.model.JavaClass;
import jdepend.parse.BuildListener;
import jdepend.parse.ParseConfigurator;
import jdepend.parse.ParseData;
import jdepend.parse.ParseListener;

/**
 * Classes构建器
 * 
 * @author wangdg
 * 
 */
public abstract class AbstractClassBuilder {

	private ParseConfigurator conf;

	private ArrayList<BuildListener> buildListeners = new ArrayList<BuildListener>();

	private boolean isBuildClassRelation = true;

	protected void onClassBuild(Collection<JavaClass> classes) {
		for (Iterator<BuildListener> i = buildListeners.iterator(); i.hasNext();) {
			i.next().onBuildJavaClasses(classes);
		}
	}

	public void addBuildListener(BuildListener listener) {
		buildListeners.add(listener);
	}

	public void setConf(ParseConfigurator conf) {
		this.conf = conf;
	}

	public ParseConfigurator getConf() {
		if (this.conf == null) {
			this.conf = new ParseConfigurator();
		}
		return conf;
	}

	public boolean isBuildClassRelation() {
		return isBuildClassRelation;
	}

	public void setBuildClassRelation(boolean isBuildClassRelation) {
		this.isBuildClassRelation = isBuildClassRelation;
	}

	public abstract Collection<JavaClass> build(ParseData data);

	public abstract PackageFilter getFilter();

	public abstract void addParseListener(ParseListener listener);

	public abstract void setWriter(PrintWriter writer);
}
