package jdepend.ui;

import java.util.LinkedHashMap;
import java.util.Map;

import jdepend.core.framework.serverconf.ServerConfigurator;
import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.ui.SettingDialog;
import jdepend.framework.ui.StatusField;

/**
 * The <code>ServiceProxySettingDialog</code> displays the setting information.
 * 
 * @author <b>Abner</b>
 * 
 */

public final class ServiceProxySettingDialog extends SettingDialog {

	public ServiceProxySettingDialog(JDependCooper parent) {
		super(parent);
	}

	@Override
	protected Map<String, String> initFiles() {

		Map<String, String> files = new LinkedHashMap<String, String>();

		files.put("请选择...", "");
		files.put("服务配置", "/conf/remote.properties");

		return files;
	}

	@Override
	protected void doSaveAfter(String filePath) throws JDependException {
		if (filePath.endsWith("remote.properties")) {
			JDependContext.setIsLocalService(((new ServerConfigurator()).isLocalService()));
			if (JDependContext.isLocalService()) {
				frame.getStatusField().setText(JDependContext.Local, StatusField.Center);
			} else {
				frame.getStatusField().setText(JDependContext.Remote, StatusField.Center);
			}
		}
	}

}
