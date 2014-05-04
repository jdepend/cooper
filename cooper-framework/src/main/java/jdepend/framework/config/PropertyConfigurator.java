package jdepend.framework.config;

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
 * The <code>PropertyConfigurator</code> class contains configuration
 * information contained in the <code>jdepend.properties</code> file, if such a
 * file exists either in the user's home directory or somewhere in the
 * classpath.
 * 
 * @author <b>Abner</b>
 * 
 */

public class PropertyConfigurator implements Serializable {

	private Properties properties;

	public transient static final String DEFAULT_PROPERTY_DIR = "conf";

	public transient static final String DEFAULT_PROPERTY_FILE = "jdepend.properties";

	/**
	 * Constructs a <code>PropertyConfigurator</code> instance containing the
	 * properties specified in the file <code>jdepend.properties</code>, if it
	 * exists.
	 */
	public PropertyConfigurator() {
		this(getDefaultPropertyFile());
	}

	/**
	 * Constructs a <code>PropertyConfigurator</code> instance with the
	 * specified property set.
	 * 
	 * @param p
	 *            Property set.
	 */
	public PropertyConfigurator(Properties p) {
		this.properties = p;
	}

	/**
	 * Constructs a <code>PropertyConfigurator</code> instance with the
	 * specified property file.
	 * 
	 * @param f
	 *            Property file.
	 */
	public PropertyConfigurator(File f) {
		this(loadProperties(f));
	}

	public String getJavaClassEncode() {

		String key = "javaClassEncode";
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}

		return null;
	}

	public boolean isSaveResult() {

		String key = "isSaveResult";
		if (properties.containsKey(key)) {
			String value = properties.getProperty(key);
			return new Boolean(value).booleanValue();
		}

		return false;
	}

	public boolean isAutoSaveScore() {

		String key = "isAutoSaveScore";
		if (properties.containsKey(key)) {
			String value = properties.getProperty(key);
			return new Boolean(value).booleanValue();
		}

		return false;
	}

	public String getLogWriterClassName() {

		String key = "logWriterClassName";
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}

		return null;
	}

	public boolean isPrintSystemLog() {

		String key = "printSystemLevel";
		if (properties.containsKey(key)) {
			String value = properties.getProperty(key);
			if (value.equalsIgnoreCase("Info")) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	public boolean isPrintSystemWarning() {

		String key = "printSystemLevel";
		if (properties.containsKey(key)) {
			String value = properties.getProperty(key);
			if (value.equalsIgnoreCase("Info") || value.equalsIgnoreCase("Warning")) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	public boolean isPrintBusiLog() {

		String key = "isPrintBusiLog";
		if (properties.containsKey(key)) {
			String value = properties.getProperty(key);
			return new Boolean(value).booleanValue();
		}

		return true;
	}

	public static File getDefaultPropertyFile() {
		String home = JDependContext.getWorkspacePath() + "/" + PropertyConfigurator.DEFAULT_PROPERTY_DIR;
		return new File(home, DEFAULT_PROPERTY_FILE);
	}

	public static Properties loadProperties(File file) {

		Properties p = new Properties();

		InputStream is = null;

		try {
			is = new FileInputStream(file);
		} catch (Exception e) {
			is = PropertyConfigurator.class.getResourceAsStream("/" + DEFAULT_PROPERTY_FILE);
			if (is == null) {
				is = PropertyConfigurator.class.getResourceAsStream(DEFAULT_PROPERTY_FILE);
			}
		}

		try {
			if (is != null) {
				InputStreamReader in = new InputStreamReader(is, "UTF-8");
				p.load(in);
			} else {
				LogUtil.getInstance(PropertyConfigurator.class).systemError("没有读取到jdepend.properties配置文件。");
			}
		} catch (IOException ignore) {
			ignore.printStackTrace();
			LogUtil.getInstance(PropertyConfigurator.class).systemError("读取jdepend.properties配置文件出错。");
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
