package jdepend.core.config;

import jdepend.framework.exception.JDependException;

public class CommandConfException extends JDependException {

	private String group;
	private String command;

	public CommandConfException(String group, String command) {
		super();
		this.group = group;
		this.command = command;
	}

	public CommandConfException(String group, String command, String msg, Throwable e) {
		super(msg, e);
		this.group = group;
		this.command = command;
	}

	public CommandConfException(String group, String command, String msg) {
		super(msg);
		this.group = group;
		this.command = command;
	}

	public CommandConfException(String group, String command, Throwable e) {
		super(e);
		this.group = group;
		this.command = command;
	}

	public CommandConfException() {
		super();
	}

	public CommandConfException(String msg, Throwable e) {
		super(msg, e);
	}

	public CommandConfException(String msg) {
		super(msg);
	}

	public CommandConfException(Throwable e) {
		super(e);
	}

	@Override
	public String getMessage() {
		StringBuilder msg = new StringBuilder();
		if (this.group != null && this.group.length() > 0) {
			msg.append("出错Group[" + this.group + "]");
		}
		if (this.command != null && this.command.length() > 0) {
			msg.append("出错Command[" + this.command + "]");
		}
		msg.append("\n错误信息：\n");
		msg.append(super.getMessage());
		return msg.toString();
	}

}
