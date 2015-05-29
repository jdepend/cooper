package jdepend.ui.dialog;

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

public final class CooperSettingDialog extends SettingDialog {

	public CooperSettingDialog(JDependCooper parent) {
		super(parent);
	}

	@Override
	protected Map<String, String> initFiles() {

		Map<String, String> files = new LinkedHashMap<String, String>();

		files.put("请选择...", "");
		files.put("系统配置文件", "\\conf\\jdepend.properties");
		files.put("文件解析配置文件", "\\conf\\parse.properties");
		files.put("服务配置文件", "\\conf\\service.properties");
		files.put("忽略的包列表", "\\conf\\ignore.properties");
		files.put("报告输出配置文件", "\\conf\\reportFilter.ini");
		files.put("操作命令模板配置文件", "\\conf\\command.xml");
		files.put("UI配置文件", "\\conf\\ui.properties");

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
