package jdepend.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Properties;

import jdepend.framework.context.JDependContext;
import jdepend.framework.log.LogUtil;
import jdepend.metadata.profile.JavaClassRelationItemProfile;
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
public class ParseConfigurator implements Serializable{

	private static final long serialVersionUID = 279725338071328382L;

	private Properties properties;

	public transient static final String DEFAULT_PROPERTY_DIR = "conf";

	public transient static final String DEFAULT_PROPERTY_FILE = "parse.properties";

	private PackageFilter packageFilter;

	private JavaClassRelationTypes javaClassRelationTypes;

	public ParseConfigurator() {
		this(new DefaultJavaClassRelationItemProfile());
	}
	
	public ParseConfigurator(JavaClassRelationItemProfile javaClassRelationItemProfile){
		this(getDefaultPropertyFile());
		// 装载全局过滤包列表
		this.packageFilter = (new FilteredPackageConfigurator()).getPackageFilter();
		this.javaClassRelationTypes = javaClassRelationItemProfile.getJavaClassRelationTypes();
	}

	private ParseConfigurator(Properties p) {
		this.properties = p;
	}

	private ParseConfigurator(File f) {
		this(loadProperties(f));
	}

	public PackageFilter getPackageFilter() {
		return packageFilter;
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

	public String[] getHttpInvokeClassNames() {

		String key = "httpInvokeClassNames";
		if (properties.containsKey(key)) {
			String value = properties.getProperty(key);
			return value.split(",");
		}

		return null;
	}

	public JavaClassRelationTypes getJavaClassRelationTypes() {
		return javaClassRelationTypes;
	}

	private static File getDefaultPropertyFile() {
		String home = JDependContext.getWorkspacePath() + "/" + ParseConfigurator.DEFAULT_PROPERTY_DIR;
		return new File(home, DEFAULT_PROPERTY_FILE);
	}

	private static Properties loadProperties(File file) {

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
