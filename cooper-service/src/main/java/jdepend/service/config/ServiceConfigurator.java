package jdepend.service.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Properties;

import jdepend.framework.context.JDependContext;
import jdepend.framework.log.LogUtil;

/**
 * 服务配置信息
 * 
 * @author wangdg
 * 
 */
public class ServiceConfigurator implements Serializable{

	private static final long serialVersionUID = -6121500160446113502L;

	private Properties properties;

	public transient static final String DEFAULT_PROPERTY_DIR = "conf";

	public transient static final String DEFAULT_PROPERTY_FILE = "service.properties";

	public ServiceConfigurator() {
		this(getDefaultPropertyFile());
	}

	private ServiceConfigurator(Properties p) {
		this.properties = p;
	}

	private ServiceConfigurator(File f) {
		this(loadProperties(f));
	}

	public boolean isSaveResult() {

		String key = "isSaveResult";
		if (properties.containsKey(key)) {
			String value = properties.getProperty(key);
			return new Boolean(value).booleanValue();
		}

		return true;
	}

	public boolean enableAbstractClassCountQualificationConfirmer() {

		String key = "enableAbstractClassCountQualificationConfirmer";
		if (properties.containsKey(key)) {
			String value = properties.getProperty(key);
			return new Boolean(value).booleanValue();
		}

		return true;
	}

	public boolean isCalJavaClassCycle() {

		String key = "isCalJavaClassCycle";
		if (properties.containsKey(key)) {
			String value = properties.getProperty(key);
			return new Boolean(value).booleanValue();
		}

		return true;
	}

	public static File getDefaultPropertyFile() {
		String home = JDependContext.getWorkspacePath() + "/" + ServiceConfigurator.DEFAULT_PROPERTY_DIR;
		return new File(home, DEFAULT_PROPERTY_FILE);
	}

	public static Properties loadProperties(File file) {

		Properties p = new Properties();

		InputStream is = null;

		try {
			is = new FileInputStream(file);
		} catch (Exception e) {
			is = ServiceConfigurator.class.getResourceAsStream("/" + DEFAULT_PROPERTY_FILE);
			if (is == null) {
				is = ServiceConfigurator.class.getResourceAsStream(DEFAULT_PROPERTY_FILE);
			}
		}

		try {
			if (is != null) {
				InputStreamReader in = new InputStreamReader(is, "UTF-8");
				p.load(in);
			} else {
				LogUtil.getInstance(ServiceConfigurator.class).systemError("没有读取到service.properties配置文件。");
			}
		} catch (IOException ignore) {
			ignore.printStackTrace();
			LogUtil.getInstance(ServiceConfigurator.class).systemError("读取service.properties配置文件出错。");
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
