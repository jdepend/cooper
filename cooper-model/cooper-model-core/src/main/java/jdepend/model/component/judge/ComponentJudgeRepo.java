package jdepend.model.component.judge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.model.ComponentException;
import jdepend.model.component.modelconf.GroupComponentModelConf;

public class ComponentJudgeRepo {

	private String group;

	private String command;

	private JudgeConfigure conf = new JudgeConfigure();

	public ComponentJudgeRepo(String group, String command) throws ComponentException {

		this.group = group;
		this.command = command;

		File file = getDefaultPropertyFile();

		if (file.exists()) {
			Properties properties = new Properties();
			FileInputStream is = null;
			try {
				is = new FileInputStream(file);
				properties.load(is);
				this.initConfigure(properties);
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.getInstance(ComponentJudgeRepo.class).systemError("读取componentJudge配置信息出错。");
				throw new ComponentException("读取componentJudge配置信息出错。", e);
			} finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException ignore) {
				}
			}
		}
	}

	private void initConfigure(Properties properties) {
		String applyChildren = (String) properties.get("applyChildren");
		if (applyChildren != null) {
			conf.applyChildren = Boolean.parseBoolean(applyChildren);
		} else {
			conf.applyChildren = false;
		}
		String childrenKeys = (String) properties.get("childrenKeys");
		if (childrenKeys != null) {
			for (String key : childrenKeys.split(",")) {
				conf.childrenKeys.add(key);
			}
		}

		String applyLayer = (String) properties.get("applyLayer");
		if (applyLayer != null) {
			conf.applyLayer = Boolean.parseBoolean(applyLayer);
		} else {
			conf.applyLayer = false;
		}

		String layer = (String) properties.get("layer");
		if (layer != null && layer.length() > 0) {
			conf.layer = Integer.parseInt(layer);
		}
	}

	private File getDefaultPropertyFile() {
		String home = JDependContext.getWorkspacePath() + "/" + GroupComponentModelConf.DEFAULT_PROPERTY_DIR;

		String fileName = "componentJudge_" + group + "_" + command + ".properties";
		return new File(home, fileName);
	}

	

	public JudgeConfigure getConfigure() {
		return conf;
	}

	public void save(JudgeConfigure conf) throws JDependException {
		this.conf = conf;
		this.save();
	}

	private void save() throws JDependException {

		FileOutputStream os = null;
		Properties properties = new Properties();
		try {
			properties.put("applyChildren", String.valueOf(conf.applyChildren));
			properties.put("childrenKeys", String.valueOf(conf.getChildrenKeys()));
			properties.put("applyLayer", String.valueOf(conf.applyLayer));
			if (conf.layer != null) {
				properties.put("layer", String.valueOf(conf.layer));
			} else {
				properties.put("layer", "");
			}

			File file = getDefaultPropertyFile();
			if (!file.exists()) {
				file.createNewFile();
			}
			os = new FileOutputStream(file);
			properties.store(os, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.getInstance(ComponentJudgeRepo.class).systemError("保存componentJudge配置信息出错。");
			throw new JDependException("保存componentJudge配置信息出错。", e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
