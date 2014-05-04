package jdepend.parse.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import jdepend.framework.context.JDependContext;
import jdepend.framework.log.LogUtil;
import jdepend.model.relationtype.JavaClassRelationTypeMgr;

/**
 * 解析配置信息
 * 
 * @author wangdg
 * 
 */
public class ParseConfigurator implements Serializable {

	private static final long serialVersionUID = 4818843046017316979L;

	private Properties properties;

	public transient static final String DEFAULT_PROPERTY_DIR = "conf";

	public transient static final String DEFAULT_PROPERTY_FILE = "parse.properties";

	private Collection<String> filteredPackages;

	private Collection<String> notFilteredPackages;

	public ParseConfigurator() {
		this(getDefaultPropertyFile());
		// 装载全局过滤包列表
		this.filteredPackages = (new FilteredPackageConfigurator()).getFilteredPackages();
		this.notFilteredPackages = (new FilteredPackageConfigurator()).getNotFilteredPackages();
	}

	private ParseConfigurator(Properties p) {
		this.properties = p;
	}

	private ParseConfigurator(File f) {
		this(loadProperties(f));
	}

	public Collection<String> getFilteredPackages() {
		return filteredPackages;
	}

	public Collection<String> getNotFilteredPackages() {
		return notFilteredPackages;
	}

	public boolean getAnalyzeInnerClasses() {

		String key = "analyzeInnerClasses";
		if (properties.containsKey(key)) {
			String value = properties.getProperty(key);
			return new Boolean(value).booleanValue();
		}

		return true;
	}

	public boolean getParseDebug() {

		String key = "parseDebug";
		if (properties.containsKey(key)) {
			String value = properties.getProperty(key);
			return new Boolean(value).booleanValue();
		}

		return false;
	}

	public boolean getEveryClassBuild() {

		String key = "everyClassBuild";
		if (properties.containsKey(key)) {
			String value = properties.getProperty(key);
			return new Boolean(value).booleanValue();
		}

		return false;
	}

	public String getClassFileParser() {

		String key = "ClassFileParser";
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}

		return null;
	}

	public Collection<String> getCreateRelationTypes() {

		Collection<String> relationTypes = new HashSet<String>();

		List<String> allRelationTypes = new ArrayList<String>();
		allRelationTypes.add(JavaClassRelationTypeMgr.Inherit);
		allRelationTypes.add(JavaClassRelationTypeMgr.Field);
		allRelationTypes.add(JavaClassRelationTypeMgr.Param);
		allRelationTypes.add(JavaClassRelationTypeMgr.Variable);
		allRelationTypes.add(JavaClassRelationTypeMgr.Table);

		for (String relationType : allRelationTypes) {
			if (properties.containsKey(relationType)) {
				if (new Boolean(properties.getProperty(relationType)).booleanValue()) {
					relationTypes.add(relationType);
				}
			} else {
				relationTypes.add(relationType);
			}
		}

		return relationTypes;

	}

	public boolean enableAbstractClassCountQualificationConfirmer() {

		String key = "enableAbstractClassCountQualificationConfirmer";
		if (properties.containsKey(key)) {
			String value = properties.getProperty(key);
			return new Boolean(value).booleanValue();
		}

		return false;
	}

	public static File getDefaultPropertyFile() {
		String home = JDependContext.getWorkspacePath() + "/" + ParseConfigurator.DEFAULT_PROPERTY_DIR;
		return new File(home, DEFAULT_PROPERTY_FILE);
	}

	public static Properties loadProperties(File file) {

		Properties p = new Properties();

		InputStream is = null;

		try {
			is = new FileInputStream(file);
		} catch (Exception e) {
			is = ParseConfigurator.class.getResourceAsStream("/" + DEFAULT_PROPERTY_FILE);
			if (is == null) {
				is = ParseConfigurator.class.getResourceAsStream(DEFAULT_PROPERTY_FILE);
			}
		}

		try {
			if (is != null) {
				InputStreamReader in = new InputStreamReader(is, "UTF-8");
				p.load(in);
			} else {
				LogUtil.getInstance(ParseConfigurator.class).systemError("没有读取到parse.properties配置文件。");
			}
		} catch (IOException ignore) {
			ignore.printStackTrace();
			LogUtil.getInstance(ParseConfigurator.class).systemError("读取parse.properties配置文件出错。");
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException ignore) {
			}
		}

		return p;
	}
}
