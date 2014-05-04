package jdepend.ui.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jdepend.framework.context.JDependContext;
import jdepend.framework.domain.PersistentBean;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.UIProperty;

/**
 * UI相关的配置信息和操作信息
 * 
 * @author user
 *
 */
public class UIPropertyConfigurator extends PersistentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5460667730699202795L;

	private transient Properties properties;

	public transient static final String DEFAULT_PROPERTY_DIR = "conf";

	public transient static final String DEFAULT_PROPERTY_FILE = "ui.properties";

	public transient final static String GroupTabLayoutPolicy_WRAP_TAB_LAYOUT = "WRAP_TAB_LAYOUT";
	public transient final static String GroupTabLayoutPolicy_SCROLL_TAB_LAYOUT = "SCROLL_TAB_LAYOUT";

	public transient final static String defaultAnalyzerType = "关注";

	public transient final static int defaultMaxRelation = -1;

	private transient final static int defaultTextFontSize = 14;

	private Map<String, UIOperationData> UIOperationDatas;

	private transient static UIPropertyConfigurator inst = null;

	public static UIPropertyConfigurator getInstance() {
		if (inst == null) {
			inst = new UIPropertyConfigurator("UI配置信息", "UI配置信息", DEFAULT_PROPERTY_DIR);
		}
		return inst;

	}

	private void loadProperties() {
		this.properties = loadProperties(getDefaultPropertyFile());
	}

	public UIPropertyConfigurator() {
		this.loadProperties();
	}

	public UIPropertyConfigurator(String string, String string2, String defaultPropertyDir) {
		super(string, string2, defaultPropertyDir);
		this.loadProperties();
		if (this.UIOperationDatas == null) {
			this.UIOperationDatas = new HashMap<String, UIOperationData>();
		}
		if (this.UIOperationDatas.size() == 0) {
			UIOperationData data = new UIOperationData();
			this.UIOperationDatas.put(JDependContext.getWorkspacePath(), data);
		}

	}

	public String getGroupTabLayoutPolicy() {

		String key = "groupTabLayoutPolicy";
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}

		return GroupTabLayoutPolicy_WRAP_TAB_LAYOUT;
	}

	public String getDefaultAnalyzerType() {

		String key = "defaultAnalyzerType";
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}

		return defaultAnalyzerType;
	}

	public int getMaxRelations() {

		String key = "maxRelations";
		if (properties.containsKey(key)) {
			return Integer.parseInt(properties.getProperty(key));
		}

		return defaultMaxRelation;
	}

	public int getPopupSummary() {

		String key = "popupSummary";
		if (properties.containsKey(key)) {
			return Integer.parseInt(properties.getProperty(key));
		}

		return 0;
	}

	public int getTextFontSize() {
		String key = "TextFontSize";
		if (properties.containsKey(key)) {
			return Integer.parseInt(properties.getProperty(key));
		}

		return defaultTextFontSize;
	}

	public boolean isVisibleCircle() {

		String key = "isVisibleCircle";
		if (properties.containsKey(key)) {
			return Boolean.parseBoolean(properties.getProperty(key));
		}

		return false;
	}

	public void setDefaultTab(int one, int two) throws IOException {
		this.obtainUIOperationData().setDefaultTabOneIndex(one);
		this.obtainUIOperationData().setDefaultTabTwoIndex(two);
	}

	public Integer obtainGroupIndex() {
		return this.obtainUIOperationData().getGroupIndex();
	}

	public void setGroupIndex(Integer groupIndex) {
		this.obtainUIOperationData().setGroupIndex(groupIndex);
	}

	private UIOperationData obtainUIOperationData() {
		UIOperationData data = this.UIOperationDatas.get(JDependContext.getWorkspacePath());
		if (data == null) {
			data = new UIOperationData();
			this.UIOperationDatas.put(JDependContext.getWorkspacePath(), data);
		}
		return data;
	}

	public Map<String, UIOperationData> getUIOperationDatas() {
		return UIOperationDatas;
	}

	public void setUIOperationDatas(Map<String, UIOperationData> uIOperationDatas) {
		UIOperationDatas = uIOperationDatas;
	}

	public static File getDefaultPropertyFile() {
		String home = JDependContext.getWorkspacePath() + "/" + UIPropertyConfigurator.DEFAULT_PROPERTY_DIR;
		return new File(home, DEFAULT_PROPERTY_FILE);
	}

	public static Properties loadProperties(File file) {

		Properties p = new Properties();

		InputStream is = null;

		try {
			is = new FileInputStream(file);
		} catch (Exception e) {
			is = UIPropertyConfigurator.class.getResourceAsStream("/" + DEFAULT_PROPERTY_FILE);
			if (is == null) {
				is = UIPropertyConfigurator.class.getResourceAsStream(DEFAULT_PROPERTY_FILE);
			}
		}

		try {
			if (is != null) {
				InputStreamReader in = new InputStreamReader(is, "UTF-8");
				p.load(in);
			} else {
				LogUtil.getInstance(UIPropertyConfigurator.class).systemError("没有读取到ui.properties配置文件。");
			}
		} catch (IOException ignore) {
			ignore.printStackTrace();
			LogUtil.getInstance(UIPropertyConfigurator.class).systemError("读取ui.properties配置文件出错。");
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

	public int obtainDefaultTabOneIndex() {
		return this.obtainUIOperationData().getDefaultTabOneIndex();
	}

	public void setDefaultTabOneIndex(int defaultTabOneIndex) {
		this.obtainUIOperationData().setDefaultTabOneIndex(defaultTabOneIndex);
	}

	public int obtainDefaultTabTwoIndex() {
		return this.obtainUIOperationData().getDefaultTabTwoIndex();
	}

	public void setDefaultTabTwoIndex(int defaultTabTwoIndex) {
		this.obtainUIOperationData().setDefaultTabTwoIndex(defaultTabTwoIndex);
	}

	public void refresh() {
		this.loadProperties();
		// 设置字体大小
		UIProperty.setSize(this.getTextFontSize());
	}

}
