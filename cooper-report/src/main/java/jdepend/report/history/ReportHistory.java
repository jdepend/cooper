package jdepend.report.history;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import jdepend.framework.context.JDependContext;
import jdepend.framework.exception.JDependException;
import jdepend.framework.log.LogUtil;
import jdepend.framework.util.FileUtil;

public class ReportHistory {

	public final static String dir = "history";

	private String group;

	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public ReportHistory() {
	}

	public ReportHistory(String group) {
		this.group = group;
	}

	public void save(String command, StringBuilder content, String tip) throws JDependException {
		// 处理文件信息
		List<ReportHistoryItemInfo> infos = load(command);

		ReportHistoryItemInfo info = new ReportHistoryItemInfo();
		info.key = nextKey(command, infos);
		info.date = Calendar.getInstance().getTime();
		info.path = info.key + ".txt";
		info.tip = tip;

		infos.add(info);

		saveFileInfo(command, infos);

		// 保存文件
		String path = JDependContext.getWorkspacePath() + "//" + dir + "//" + this.group + "//" + command + "//"
				+ info.path;

		try {
			FileUtil.saveFileContent(path, content);
		} catch (JDependException e) {
			e.printStackTrace();
			LogUtil.getInstance(ReportHistory.class).systemError("保存报告[" + command + info.key + "]历史内容失败！");
			throw new JDependException("保存报告[" + command + info.key + "]历史内容失败！", e);
		}
	}

	public void deleteGroup() throws JDependException {
		deleteGroup(group);
	}

	public void deleteGroup(String group) throws JDependException {
		String path = JDependContext.getWorkspacePath() + "//" + dir + "//" + group;
		FileUtil.deleteFile(path);
	}

	public void delete(String version) throws JDependException {

		String key = version.substring(0, version.indexOf('_'));
		// 删除文件信息
		List<ReportHistoryItemInfo> infos = load(key);

		ReportHistoryItemInfo info = new ReportHistoryItemInfo();
		info.key = version;

		infos.remove(info);

		saveFileInfo(key, infos);
		// 保存文件
		(new File(this.getFilePath(version))).delete();
	}

	public StringBuilder getContent(String version) throws JDependException {

		try {
			return FileUtil.readFileContent(this.getFilePath(version));
		} catch (JDependException e) {
			e.printStackTrace();
			LogUtil.getInstance(ReportHistory.class).systemError("读取报告[" + version + "]历史内容失败！");
			throw new JDependException("读取报告[" + version + "]历史内容失败！", e);
		}

	}

	public String getFilePath(String version) {

		String key = version.substring(0, version.indexOf('_'));
		String path = JDependContext.getWorkspacePath() + "//" + dir + "//" + this.group + "//" + key + "//" + version
				+ ".txt";

		return path;
	}

	private void saveFileInfo(String command, List<ReportHistoryItemInfo> infos) throws JDependException {
		Properties content = new Properties();
		StringBuilder value;
		for (ReportHistoryItemInfo info : infos) {
			value = new StringBuilder(100);
			value.append(df.format(info.date));
			value.append(",");
			value.append(info.tip);
			value.append(",");
			value.append(info.path);
			value.append(",");
			value.append(info.tip);

			content.put(info.key, value.toString());
		}
		String path = JDependContext.getWorkspacePath() + "//" + dir + "//" + group + "//" + command + "//" + command
				+ ".xml";

		try {
			FileUtil.saveFileContent(path, content);
		} catch (JDependException e) {
			e.printStackTrace();
			LogUtil.getInstance(ReportHistory.class).systemError("保存报告[" + command + "]历史摘要失败！");
			throw new JDependException("保存报告[" + command + "]历史内容失败！", e);
		}

	}

	private String nextKey(String key, List<ReportHistoryItemInfo> infos) {

		int max = 0;
		int index;
		for (ReportHistoryItemInfo info : infos) {
			index = Integer.parseInt(info.key.substring(info.key.indexOf('_') + 1));
			if (max < index) {
				max = index;
			}
		}
		max++;
		return key + '_' + max;
	}

	public List<ReportHistoryItemInfo> load(String command) {

		// if (key == null)
		// return new ArrayList<ReportHistoryItemInfo>();

		// 创建路径
		String path = JDependContext.getWorkspacePath() + "//" + dir + "//" + group;

		File file = new File(path);

		if (!file.exists()) {
			file.mkdirs();
		}

		path += "//" + command;

		file = new File(path);

		if (!file.exists()) {
			file.mkdirs();
		}

		// load history
		List<ReportHistoryItemInfo> itemInfos = new ArrayList<ReportHistoryItemInfo>();

		path = JDependContext.getWorkspacePath() + "//" + dir + "//" + group + "//" + command + "//" + command + ".xml";

		Properties content = new Properties();

		try {
			content.loadFromXML(new FileInputStream(path));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			try {
				content.storeToXML(new FileOutputStream(path), null);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 转化信息
		String item;
		String[] infos;
		ReportHistoryItemInfo itemInfo;

		Iterator it = content.keySet().iterator();
		while (it.hasNext()) {
			item = (String) it.next();
			infos = ((String) content.get(item)).split(",");

			itemInfo = new ReportHistoryItemInfo();
			itemInfo.key = item;
			try {
				itemInfo.date = df.parse(infos[0]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			itemInfo.tip = infos[1];
			itemInfo.path = infos[2];

			itemInfos.add(itemInfo);
		}
		return itemInfos;
	}
}
