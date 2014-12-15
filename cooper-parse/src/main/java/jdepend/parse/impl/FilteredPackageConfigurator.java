package jdepend.parse.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

import jdepend.framework.context.JDependContext;
import jdepend.framework.log.LogUtil;

/**
 * 全局过滤掉的包列表
 * 
 * @author wangdg
 * 
 */
public class FilteredPackageConfigurator implements Serializable {

	private Properties properties;

	public transient static final String DEFAULT_PROPERTY_DIR = "conf";

	public transient static final String DEFAULT_PROPERTY_FILE = "ignore.properties";

	public FilteredPackageConfigurator() {
		this(getDefaultPropertyFile());
	}

	public PackageFilter getPackageFilter() {
		return new PackageFilter(this.getFilteredPackages(), this.getNotFilteredPackages());
	}

	private FilteredPackageConfigurator(Properties p) {
		this.properties = p;
	}

	private FilteredPackageConfigurator(File f) {
		this(loadProperties(f));
	}

	private Collection<String> getFilteredPackages() {

		Collection<String> packages = new ArrayList<String>();

		Enumeration e = properties.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			if (key.startsWith("ignore")) {
				String path = properties.getProperty(key);
				StringTokenizer st = new StringTokenizer(path, ",");
				while (st.hasMoreTokens()) {
					String name = (String) st.nextToken();
					name = name.trim();
					if (!name.startsWith("not")) {
						packages.add(name);
					}
				}
			}
		}

		return packages;
	}

	private Collection<String> getNotFilteredPackages() {
		Collection<String> packages = new ArrayList<String>();

		Enumeration e = properties.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			if (key.startsWith("ignore")) {
				String path = properties.getProperty(key);
				StringTokenizer st = new StringTokenizer(path, ",");
				while (st.hasMoreTokens()) {
					String name = (String) st.nextToken();
					name = name.trim();
					if (name.startsWith("not")) {
						StringTokenizer st1 = new StringTokenizer(name);// 空格分隔多个not
																		// filter
																		// 的包
						while (st1.hasMoreTokens()) {
							String ext = (String) st1.nextToken();
							ext = ext.trim();
							if (!ext.equals("not")) {
								packages.add(ext);
							}
						}
					}
				}
			}
		}

		return packages;
	}

	private static File getDefaultPropertyFile() {
		String home = JDependContext.getWorkspacePath() + "/" + FilteredPackageConfigurator.DEFAULT_PROPERTY_DIR;
		return new File(home, DEFAULT_PROPERTY_FILE);
	}

	private static Properties loadProperties(File file) {

		Properties p = new Properties();

		InputStream is = null;

		try {
			is = new FileInputStream(file);
		} catch (Exception e) {
			is = FilteredPackageConfigurator.class.getResourceAsStream("/" + DEFAULT_PROPERTY_FILE);
			if (is == null) {
				is = FilteredPackageConfigurator.class.getResourceAsStream(DEFAULT_PROPERTY_FILE);
			}
		}

		try {
			if (is != null) {
				InputStreamReader in = new InputStreamReader(is, "UTF-8");
				p.load(in);
			} else {
				LogUtil.getInstance(FilteredPackageConfigurator.class).systemError("没有读取到ignore.properties配置文件。");
			}
		} catch (IOException ignore) {
			ignore.printStackTrace();
			LogUtil.getInstance(FilteredPackageConfigurator.class).systemError("读取ignore.properties配置文件出错。");
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
