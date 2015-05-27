package jdepend.core.remote.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.context.JDependContext;
import jdepend.framework.log.LogUtil;

/**
 * The <code>ServiceProxyConfigurator</code> class contains configuration
 * information contained in the <code>remote.properties</code> file, if such a
 * file exists either in the user's home directory or somewhere in the
 * classpath.
 * 
 * @author <b>Abner</b>
 * 
 */

public class ServerConfigurator {

	private Properties properties;

	public transient static final String DEFAULT_PROPERTY_FILE = "remote.properties";

	/**
	 * Constructs a <code>ServiceProxyConfigurator</code> instance containing
	 * the properties specified in the file <code>remote.properties</code>, if
	 * it exists.
	 */
	public ServerConfigurator() {
		this(getDefaultPropertyFile());
	}

	/**
	 * Constructs a <code>ServiceConfigurator</code> instance with the specified
	 * property set.
	 * 
	 * @param p
	 *            Property set.
	 */
	public ServerConfigurator(Properties p) {
		this.properties = p;
	}

	/**
	 * Constructs a <code>ServiceConfigurator</code> instance with the specified
	 * property file.
	 * 
	 * @param f
	 *            Property file.
	 */
	public ServerConfigurator(File f) {
		this(loadProperties(f));
	}

	public String getRemoteServiceURL() {

		String key = "remoteServiceURL";
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}

		return null;
	}

	public String getSessionServiceURL() {
		String key = "sessionServiceURL";
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}

		return null;
	}

	public String getUserRemoteServiceURL() {
		String key = "userRemoteServiceURL";
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}

		return null;
	}

	public String getAnalyzerServiceURL() {
		String key = "analyzerServiceURL";
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}

		return null;
	}

	public String getScoreRemoteServiceURL() {
		String key = "scoreRemoteServiceURL";
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}

		return null;
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
			is = ServerConfigurator.class.getResourceAsStream("/" + DEFAULT_PROPERTY_FILE);
			if (is == null) {
				is = ServerConfigurator.class.getResourceAsStream(DEFAULT_PROPERTY_FILE);
			}
		}

		try {
			if (is != null) {
				InputStreamReader in = new InputStreamReader(is, "UTF-8");
				p.load(in);
			} else {
				LogUtil.getInstance(ServerConfigurator.class).systemError("没有读取到remote.properties配置文件。");
			}
		} catch (IOException ignore) {
			ignore.printStackTrace();
			LogUtil.getInstance(ServerConfigurator.class).systemError("读取remote.properties配置文件出错。");
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
