package jdepend.report.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.context.JDependContext;
import jdepend.framework.log.LogUtil;

/**
 * 有状态对象
 * 
 * @author <b>Abner</b>
 * 
 */
public class ReportFilter {

	Properties switchInfo = new Properties();

	public transient static final String DEFAULT_PROPERTY_FILE = "reportFilter.ini";

	public ReportFilter() {
		switchInfo = ReportFilter.loadProperties(ReportFilter.getDefaultPropertyFile());
	}

	public boolean isComponentSummary() {
		if (switchInfo.size() == 0)
			return true;

		return "true".equals(switchInfo.get("ComponentSummary")) || "1".equals(switchInfo.get("ComponentSummary"))
				|| "Y".equalsIgnoreCase((String) switchInfo.get("ComponentSummary"));
	}

	public boolean isClassInfo() {
		if (switchInfo.size() == 0)
			return true;

		return "true".equals(switchInfo.get("ClassInfo")) || "1".equals(switchInfo.get("ClassInfo"))
				|| "Y".equalsIgnoreCase((String) switchInfo.get("ClassInfo"));
	}

	public boolean isClassSummary() {
		if (switchInfo.size() == 0)
			return true;

		return "true".equals(switchInfo.get("ClassSummary")) || "1".equals(switchInfo.get("ClassSummary"))
				|| "Y".equalsIgnoreCase((String) switchInfo.get("ClassSummary"));
	}

	public boolean isClassDetail() {
		if (switchInfo.size() == 0)
			return true;

		return "true".equals(switchInfo.get("ClassDetail")) || "1".equals(switchInfo.get("ClassDetail"))
				|| "Y".equalsIgnoreCase((String) switchInfo.get("ClassDetail"));
	}

	public static File getDefaultPropertyFile() {
		String home = JDependContext.getWorkspacePath() + "\\" + PropertyConfigurator.DEFAULT_PROPERTY_DIR;
		return new File(home, DEFAULT_PROPERTY_FILE);
	}

	public static Properties loadProperties(File file) {

		Properties p = new Properties();

		InputStream is = null;

		try {

			is = new FileInputStream(file);

		} catch (Exception e) {
			is = ReportFilter.class.getResourceAsStream("/" + DEFAULT_PROPERTY_FILE);
			if (is == null) {
				is = ReportFilter.class.getResourceAsStream(DEFAULT_PROPERTY_FILE);
			}
		}

		try {
			if (is != null) {
				p.load(is);
			} else {
				LogUtil.getInstance(ReportFilter.class).systemError("没有读取到reportFilter.ini配置文件。");
			}
		} catch (IOException ignore) {
			ignore.printStackTrace();
			LogUtil.getInstance(ReportFilter.class).systemError("读取reportFilter.ini配置文件出错。");
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
