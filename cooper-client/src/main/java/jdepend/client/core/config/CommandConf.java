package jdepend.client.core.config;

import java.util.ArrayList;
import java.util.List;

import jdepend.metadata.util.ClassSearchUtil;
import jdepend.model.component.CustomComponent;

public class CommandConf implements Comparable<CommandConf> {

	public String group;

	private String path;

	public int order;

	public String label;

	public String[] args;

	public String tip;

	public static final String DEFAULT_CLASSES = "self";

	public String getPath() {
		return covertDefaultClassesPath(path);
	}

	public void setPath(String path) {
		this.path = path;
	}

	public static CommandConf create(String component, String group) {

		CommandConf info = new CommandConf();

		info.group = group;
		info.label = "以" + component + "组件为单位生成报告";
		info.tip = info.label;

		String[] args = new String[3];
		args[0] = "-componentClassName";
		args[1] = CustomComponent.class.getName();
		args[2] = component;

		info.args = args;

		return info;
	}

	public String getArgInfo() {

		if (args == null) {
			return null;
		} else {
			StringBuilder argInfo = new StringBuilder();
			for (int i = 0; i < this.args.length; i++) {
				if (i == 0) {
					argInfo.append(this.args[i]);
				} else {
					argInfo.append(" " + this.args[i]);
				}
			}
			return argInfo.toString();
		}
	}

	public static String[] parseArgs(String info) {
		List<String> args = new ArrayList<String>();
		if (info == null || info.trim().length() == 0) {
			return null;
		} else {
			for (String item : info.split("\\s{1,}")) {
				args.add(item);
			}
			String[] rtn = new String[args.size()];
			return args.toArray(rtn);
		}
	}

	public int compareTo(CommandConf arg0) {
		return ((Integer) this.order).compareTo(arg0.order);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CommandConf other = (CommandConf) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder content = new StringBuilder(100);

		content.append("	Group Name : ");
		content.append(group);
		content.append("\n");
		content.append("	Order : ");
		content.append(order);
		content.append("\n");
		content.append("	Label : ");
		content.append(label);
		content.append("\n");
		content.append("	Tip : ");
		content.append(tip);
		content.append("\n");
		content.append("	path : ");
		content.append(getPath());
		content.append("\n");
		content.append("	args : ");

		content.append(getArgInfo());

		content.append("\n\n");

		return content.toString();
	}

	public static String covertDefaultClassesPath(String path) {
		if (CommandConf.DEFAULT_CLASSES.equalsIgnoreCase(path)) {
			StringBuilder rtn = new StringBuilder();
			for (String p : ClassSearchUtil.getSelfPath()) {
				rtn.append(p);
				rtn.append(";");
			}
			return rtn.delete(rtn.length() - 1, rtn.length()).toString();
		}
		return path;
	}

}
