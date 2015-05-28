package jdepend.ui.wizard;

import java.util.LinkedHashMap;
import java.util.Map;

import jdepend.framework.config.PropertyConfigurator;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.BusiLogUtil;
import jdepend.framework.log.LogUtil;
import jdepend.framework.ui.dialog.SettingDialog;
import jdepend.ui.JDependCooper;
import jdepend.ui.framework.UIPropertyConfigurator;

/**
 * The <code>SettingDialog</code> displays the setting information.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class IgnoreSettingDialog extends SettingDialog {

	public IgnoreSettingDialog(JDependCooper parent) {
		super(parent);
	}

	@Override
	protected Map<String, String> initFiles() {

		Map<String, String> files = new LinkedHashMap<String, String>();
		files.put("忽略的包列表", "/conf/ignore.properties");
		return files;
	}

	@Override
	protected void doSaveAfter(String filePath) throws JDependException {

		if (filePath.endsWith("jdepend.properties")) {
			PropertyConfigurator conf = new PropertyConfigurator();
			BusiLogUtil.BUSINESSLOG = conf.isPrintBusiLog();
			LogUtil.SYSTEMLOG = conf.isPrintSystemLog();
			LogUtil.SYSTEMWARNING = conf.isPrintSystemWarning();
		} else if (filePath.endsWith("ui.properties")) {
			UIPropertyConfigurator.getInstance().refresh();
			((JDependCooper) frame).refreshLayout();
		}

	}
}
