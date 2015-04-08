package jdepend.ui;

import java.util.LinkedHashMap;
import java.util.Map;

import jdepend.framework.ui.SettingDialog;

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
}
