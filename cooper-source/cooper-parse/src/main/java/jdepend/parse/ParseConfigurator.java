package jdepend.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jdepend.framework.context.JDependContext;
import jdepend.framework.log.LogUtil;
import jdepend.metadata.profile.defaultvalue.DefaultJavaClassRelationItemProfile;
import jdepend.metadata.relationtype.JavaClassRelationTypes;
import jdepend.parse.impl.FilteredPackageConfigurator;
import jdepend.parse.impl.PackageFilter;

/**
 * 解析配置信息
 * 
 * @author wangdg
 * 
 */
public class ParseConfigurator {

	private Map<String, String> properties;

	public transient static final String DEFAULT_PROPERTY_DIR = "conf";

	public transient static final String DEFAULT_PROPERTY_FILE = "parse.properties";

	private PackageFilter packageFilter;

	private JavaClassRelationTypes javaClassRelationTypes;

	private static final String analyzeInnerClasses = "analyzeInnerClasses";

	private static final String parseDebug = "parseDebug";

	private static final String everyClassBuild = "everyClassBuild";

	private static final String ClassFileParser = "ClassFileParser";

	private static final String httpInvokeClassNames = "httpInvokeClassNames";

	private static final String analyzeModel = "analyzeModel";

	public ParseConfigurator() {
		this(new DefaultJavaClassRelationItemProfile().getJavaClassRelationTypes());
	}

	public ParseConfigurator(JavaClassRelationTypes javaClassRelationTypes) {
		this(getDefaultPropertyFile());
		// 装载全局过滤包列表
		this.packageFilter = (new FilteredPackageConfigurator()).getPackageFilter();
		this.javaClassRelationTypes = javaClassRelationTypes;
	}

	public ParseConfigurator(Map<String, String> p) {
		this.properties = p;
	}

	private ParseConfigurator(File f) {
		this(loadProperties(f));
	}

	public PackageFilter getPackageFilter() {
		return packageFilter;
	}

	public void setPackageFilter(PackageFilter packageFilter) {
		this.packageFilter = packageFilter;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public boolean getAnalyzeInnerClasses() {

		if (properties.containsKey(analyzeInnerClasses)) {
			String value = properties.get(analyzeInnerClasses);
			return new Boolean(value).booleanValue();
		}

		return true;
	}

	public boolean getParseDebug() {

		if (properties.containsKey(parseDebug)) {
			String value = properties.get(parseDebug);
			return new Boolean(value).booleanValue();
		}

		return false;
	}

	public boolean getEveryClassBuild() {

		if (properties.containsKey(everyClassBuild)) {
			String value = properties.get(everyClassBuild);
			return new Boolean(value).booleanValue();
		}

		return false;
	}

	public String getClassFileParser() {

		if (properties.containsKey(ClassFileParser)) {
			return properties.get(ClassFileParser);
		}

		return null;
	}

	public String[] getHttpInvokeClassNames() {

		if (properties.containsKey(httpInvokeClassNames)) {
			String value = properties.get(httpInvokeClassNames);
			return value.split(",");
		}

		return null;
	}

	public String getAnalyzeModel() {

		if (properties.containsKey(analyzeModel)) {
			return properties.get(analyzeModel);
		}

		return null;
	}

	public JavaClassRelationTypes getJavaClassRelationTypes() {
		return javaClassRelationTypes;
	}

	public void setJavaClassRelationTypes(JavaClassRelationTypes javaClassRelationTypes) {
		this.javaClassRelationTypes = javaClassRelationTypes;
	}

	private static File getDefaultPropertyFile() {
		String home = JDependContext.getWorkspacePath() + "/" + ParseConfigurator.DEFAULT_PROPERTY_DIR;
		return new File(home, DEFAULT_PROPERTY_FILE);
	}

	private static Map<String, String> loadProperties(File file) {

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

		return new HashMap<String, String>((Map) p);
	}
}
