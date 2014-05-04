package jdepend.server.ui;

import java.util.LinkedHashMap;
import java.util.Map;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.SettingDialog;

/**
 * The <code>ServiceSettingDialog</code> displays the setting information.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class ServiceSettingDialog extends SettingDialog {

	public ServiceSettingDialog(JDependServer parent) {
		super(parent);
	}

	@Override
	protected Map<String, String> initFiles() {

		Map<String, String> files = new LinkedHashMap<String, String>();

		files.put("请选择...", "");
		files.put("系统配置文件", "/conf/jdepend.properties");
		files.put("文件解析配置文件", "/conf/parse.properties");

		return files;
	}

	@Override
	protected void doSaveAfter(String filePath) throws JDependException {

		if (filePath.endsWith("jdepend.properties")) {
			PropertyConfigurator conf = new PropertyConfigurator();
			BusiLogUtil.BUSINESSLOG = conf.isPrintBusiLog();
			LogUtil.SYSTEMLOG = conf.isPrintSystemLog();
			LogUtil.SYSTEMWARNING = conf.isPrintSystemWarning();
		}
	}
}
